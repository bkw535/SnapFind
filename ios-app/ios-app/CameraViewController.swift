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

        previewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        previewLayer.videoGravity = .resizeAspectFill
        previewLayer.frame = view.bounds
        view.layer.insertSublayer(previewLayer, at: 0)

        DispatchQueue.global(qos: .userInitiated).async {
            self.captureSession.startRunning()
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                self.captureButton.isEnabled = true
            }
        }
    }

    // MARK: - 촬영 버튼 UI
    func setupCaptureButton() {
        captureButton = UIButton(type: .custom)
        captureButton.setImage(UIImage(systemName: "circle.fill"), for: .normal)
        captureButton.tintColor = .white
        captureButton.translatesAutoresizingMaskIntoConstraints = false
        captureButton.addTarget(self, action: #selector(capturePhoto), for: .touchUpInside)
        captureButton.isEnabled = false // 세션 준비 전까지 비활성화

        view.addSubview(captureButton)

        NSLayoutConstraint.activate([
            captureButton.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            captureButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -70),
            captureButton.widthAnchor.constraint(equalToConstant: 150),
            captureButton.heightAnchor.constraint(equalToConstant: 150)
        ])
    }

    // MARK: - 사진 촬영
    @objc func capturePhoto() {
        guard photoOutput.connections.first?.isEnabled == true else {
            print("사진 캡처 실패: 활성화된 연결 없음")
            return
        }

        let settings = AVCapturePhotoSettings()
        photoOutput.capturePhoto(with: settings, delegate: self)
    }
}

// MARK: - 사진 촬영 결과 처리
extension CameraViewController: AVCapturePhotoCaptureDelegate {
    func photoOutput(_ output: AVCapturePhotoOutput,
                     didFinishProcessingPhoto photo: AVCapturePhoto,
                     error: Error?) {

        guard let imageData = photo.fileDataRepresentation(),
              let image = UIImage(data: imageData) else {
            print("사진 처리 실패")
            return
        }

        // 이미지 백엔드로 전송 (multipart/form-data)
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

        // userId 파트 (예: 1)
        let userId = "1"
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

            print("✅ 이미지 업로드 성공")

            DispatchQueue.main.async {
                let resultVC = CrawlingResultViewController()
                resultVC.resultText = "촬영된 이미지 처리됨"
                self.navigationController?.pushViewController(resultVC, animated: true)
            }
        }
        task.resume()
        return
    }
}
