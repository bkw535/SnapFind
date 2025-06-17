//
//  ViewController.swift
//  ios-app
//
//  Created by 배건우 on 6/9/25.
//

import UIKit
import AVFoundation

class CameraViewController: UIViewController {

    var captureSession: AVCaptureSession!
    var previewLayer: AVCaptureVideoPreviewLayer!
    var photoOutput = AVCapturePhotoOutput()
    var captureButton: UIButton!

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .black

        setupCaptureButton()
        checkCameraPermission()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if captureSession != nil && !captureSession.isRunning {
            DispatchQueue.global(qos: .userInitiated).async {
                self.captureSession.startRunning()
            }
        }
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        if captureSession != nil && captureSession.isRunning {
            captureSession.stopRunning()
        }
    }

    // MARK: - 카메라 권한 확인
    func checkCameraPermission() {
        switch AVCaptureDevice.authorizationStatus(for: .video) {
        case .authorized:
            setupCamera()
        case .notDetermined:
            AVCaptureDevice.requestAccess(for: .video) { granted in
                if granted {
                    DispatchQueue.main.async {
                        self.setupCamera()
                    }
                } else {
                    print("사용자가 카메라 권한을 거부함")
                }
            }
        case .denied, .restricted:
            print("카메라 접근이 차단되어 있습니다.")
            // 사용자에게 설정에서 권한을 허용하라고 알림
        @unknown default:
            break
        }
    }

    // MARK: - 카메라 세션 설정
    func setupCamera() {
        captureSession = AVCaptureSession()
        captureSession.sessionPreset = .photo

        guard let videoDevice = AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: .back),
              let videoInput = try? AVCaptureDeviceInput(device: videoDevice),
              captureSession.canAddInput(videoInput) else {
            print("카메라 입력 장치 설정 실패")
            return
        }

        captureSession.addInput(videoInput)

        if captureSession.canAddOutput(photoOutput) {
            captureSession.addOutput(photoOutput)
        } else {
            print("카메라 출력 설정 실패")
        }

        // 👇 preview view 생성
        let previewView = UIView()
        previewView.translatesAutoresizingMaskIntoConstraints = false
        previewView.clipsToBounds = true // 중요: 영역 초과 방지
        view.insertSubview(previewView, at: 0)

        NSLayoutConstraint.activate([
            previewView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            previewView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            previewView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            previewView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -tabBarHeight())
        ])

        previewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        previewLayer.videoGravity = .resizeAspectFill
        previewView.layer.addSublayer(previewLayer)

        DispatchQueue.main.async {
            self.previewLayer.frame = previewView.bounds
        }

        DispatchQueue.global(qos: .userInitiated).async {
            self.captureSession.startRunning()
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                self.captureButton.isEnabled = true
            }
        }
    }

    private func tabBarHeight() -> CGFloat {
        return tabBarController?.tabBar.frame.height ?? 20
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        previewLayer?.frame = view.subviews.first(where: { $0.layer.sublayers?.contains(previewLayer) == true })?.bounds ?? .zero
    }

    func setupCaptureButton() {
        captureButton = UIButton(type: .custom)
        captureButton.setImage(UIImage(systemName: "circle.fill")?.withConfiguration(UIImage.SymbolConfiguration(pointSize: 70)), for: .normal)
        captureButton.tintColor = .white
        captureButton.translatesAutoresizingMaskIntoConstraints = false
        captureButton.addTarget(self, action: #selector(capturePhoto), for: .touchUpInside)
        captureButton.isEnabled = false

        view.addSubview(captureButton)

        NSLayoutConstraint.activate([
            captureButton.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            captureButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -70),
            captureButton.widthAnchor.constraint(equalToConstant: 150),
            captureButton.heightAnchor.constraint(equalToConstant: 150)
        ])
    }

    @objc func capturePhoto() {
        guard photoOutput.connections.first?.isEnabled == true else {
            print("사진 캡처 실패: 활성화된 연결 없음")
            return
        }

        let settings = AVCapturePhotoSettings()
        photoOutput.capturePhoto(with: settings, delegate: self)
    }
}

extension CameraViewController: AVCapturePhotoCaptureDelegate {
    func photoOutput(_ output: AVCapturePhotoOutput,
                     didFinishProcessingPhoto photo: AVCapturePhoto,
                     error: Error?) {

        guard let imageData = photo.fileDataRepresentation(),
              let image = UIImage(data: imageData) else {
            print("사진 처리 실패")
            return
        }

        // 1. 이메일로 사용자 id 조회
        guard let email = UserDefaults.standard.string(forKey: "userEmail") else {
            print("이메일 정보 없음")
            return
        }
        if let baseURL = Bundle.main.object(forInfoDictionaryKey: "API_BASE_URL") as? String {
            print("실제 API_BASE_URL:", baseURL)
        } else {
            print("API_BASE_URL not found")
        }
        guard let baseURL = Bundle.main.object(forInfoDictionaryKey: "API_BASE_URL") as? String else {
            print("API_BASE_URL not found")
            return
        }
        let userInfoURL = URL(string: "\(baseURL)/api/users/me?email=\(email)")!
        print(userInfoURL)
        var userInfoRequest = URLRequest(url: userInfoURL)
        userInfoRequest.httpMethod = "GET"

        let userInfoTask = URLSession.shared.dataTask(with: userInfoRequest) { [weak self] data, response, error in
            guard let self = self else { return }
            if let error = error {
                print("사용자 정보 조회 실패: \(error.localizedDescription)")
                return
            }
            guard let data = data else {
                print("데이터 없음")
                return
            }
            
            // 디버그: 서버 응답 출력
            if let responseString = String(data: data, encoding: .utf8) {
                print("서버 응답: \(responseString)")
            }
            
            do {
                let userJson = try JSONSerialization.jsonObject(with: data) as? [String: Any]
                guard let userId = userJson?["id"] as? Int64 else {
                    print("사용자 ID 파싱 실패")
                    return
                }
                
                // 2. 이미지 백엔드로 전송 (multipart/form-data)
                let url = URL(string: "https://snapfind.p-e.kr/api/search")!
                var request = URLRequest(url: url)
                request.httpMethod = "POST"

                // Multipart/form-data boundary
                let boundary = "Boundary-\(UUID().uuidString)"
                request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")

                var body = Data()

                // 이미지 파트
                body.append("--\(boundary)\r\n".data(using: .utf8)!)
                body.append("Content-Disposition: form-data; name=\"file\"; filename=\"photo.jpg\"\r\n".data(using: .utf8)!)
                body.append("Content-Type: image/jpeg\r\n\r\n".data(using: .utf8)!)
                body.append(imageData)
                body.append("\r\n".data(using: .utf8)!)

                // userId 파트 (이메일 기반으로 조회한 id)
                body.append("--\(boundary)\r\n".data(using: .utf8)!)
                body.append("Content-Disposition: form-data; name=\"userId\"\r\n\r\n".data(using: .utf8)!)
                body.append("\(userId)\r\n".data(using: .utf8)!)

                // 종료
                body.append("--\(boundary)--\r\n".data(using: .utf8)!)
                request.httpBody = body

                let task = URLSession.shared.dataTask(with: request) { data, response, error in
                    if let error = error {
                        print("이미지 업로드 실패: \(error.localizedDescription)")
                        return
                    }

                    guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                        print("이미지 업로드 실패: 서버 응답 오류")
                        return
                    }

                    guard let data = data else {
                        print("이미지 업로드 응답 데이터 없음")
                        return
                    }

                    var products: [Product] = []
                    var keyword: String?
                    do {
                        if let json = try JSONSerialization.jsonObject(with: data) as? [String: Any],
                        let keywordValue = json["keyword"] as? String,
                        let productList = json["products"] as? [[String: Any]] {
                            keyword = keywordValue
                            products = productList.compactMap { Product(dict: $0) }
                        }
                    } catch {
                        print("검색 결과 파싱 실패: \(error.localizedDescription)")
                    }

                    DispatchQueue.main.async {
                        let resultVC = CrawlingResultViewController()
                        resultVC.keyword = keyword
                        resultVC.products = products
                        print("검색 결과 페이지로 이동") // 디버그용
                        self.navigationController?.pushViewController(resultVC, animated: true)
                    }
                }
                task.resume()
            } catch {
                print("사용자 정보 파싱 실패: \(error.localizedDescription)")
            }
        }
        userInfoTask.resume()
    }
}
