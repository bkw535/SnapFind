//
//  LoginViewController.swift
//  ios-app
//
//  Created by 배건우 on 6/9/25.
//

import UIKit
import GoogleSignIn

class LoginViewController: UIViewController {
    
    private let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "SnapFind"
        label.font = UIFont.boldSystemFont(ofSize: 36)
        label.textAlignment = .center
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let loginButton: UIButton = {
        let button = UIButton(type: .system)
        button.setTitle("Google로 로그인", for: .normal)
        button.backgroundColor = .systemBlue
        button.setTitleColor(.white, for: .normal)
        button.layer.cornerRadius = 8
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
    }
    
    private func setupUI() {
        view.backgroundColor = .white
        view.addSubview(titleLabel)
        view.addSubview(loginButton)
        
        NSLayoutConstraint.activate([
            titleLabel.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 60),
            titleLabel.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            loginButton.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            loginButton.topAnchor.constraint(equalTo: titleLabel.bottomAnchor, constant: 400),
            loginButton.widthAnchor.constraint(equalToConstant: 200),
            loginButton.heightAnchor.constraint(equalToConstant: 50)
        ])
        
        loginButton.addTarget(self, action: #selector(loginButtonTapped), for: .touchUpInside)
    }
    
    func switchToMainTabBar() {
        let tabBarController = MainTabBarController()
        guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
              let window = windowScene.windows.first else { return }

        window.rootViewController = tabBarController
        window.makeKeyAndVisible()
    }
    
    @objc private func loginButtonTapped() {
        guard let clientID = Bundle.main.object(forInfoDictionaryKey: "GIDClientID") as? String else {
            print("GIDClientID not found in Info.plist")
            return
        }
        print("Using client ID: \(clientID)")
        
        let config = GIDConfiguration(clientID: clientID)
        GIDSignIn.sharedInstance.signIn(withPresenting: self) { result, error in
            if let error = error {
                print("Google Sign-In error: \(error.localizedDescription)")
                return
            }
            guard let user = result?.user else {
                print("Google Sign-In failed: No user object")
                return
            }
            let email = user.profile?.email ?? ""
            let name = user.profile?.name ?? ""
            let idToken = user.idToken?.tokenString ?? ""

            UserDefaults.standard.set(email, forKey: "userEmail")
            print("User email saved: \(email)")

            self.sendUserInfoToBackend(email: email, name: name, idToken: idToken) {
                UserDefaults.standard.set(true, forKey: "isLoggedIn")
                DispatchQueue.main.async {
                    guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
                          let window = windowScene.windows.first else { return }
                    
                    let tabBarController = MainTabBarController()
                    window.rootViewController = tabBarController
                    window.makeKeyAndVisible()
                    
                    if let cameraNav = tabBarController.viewControllers?.first as? UINavigationController {
                        let cameraVC = CameraViewController()
                        cameraNav.setViewControllers([cameraVC], animated: false)
                        tabBarController.selectedIndex = 0
                    }
                }
            }
        }
    }
    
    private func sendUserInfoToBackend(email: String, name: String, idToken: String, completion: @escaping () -> Void) {
        guard let baseURL = Bundle.main.object(forInfoDictionaryKey: "API_BASE_URL") as? String else {
            print("API_BASE_URL not found")
            return
        }
        let fullURLString = "\(baseURL)/api/users/oauth2/token"
        print("📡 요청 URL: \(fullURLString)")
        
        guard let url = URL(string: fullURLString) else {
            print("Invalid URL: \(fullURLString)")
            return
        }
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        let body: [String: String] = [
            "idToken": idToken,
            "email": email,
            "name": name
        ]
        request.httpBody = try? JSONSerialization.data(withJSONObject: body)
        
        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Network error: \(error.localizedDescription)")
                return
            }
            completion()
        }.resume()
    }
}
