//
//  LoginViewController.swift
//  ios-app
//
//  Created by 배건우 on 6/9/25.
//

import Foundation
import UIKit
import AuthenticationServices

class LoginViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white

        // Add SnapFind label at the top
        let titleLabel = UILabel()
        titleLabel.text = "SnapFind"
        titleLabel.font = UIFont.boldSystemFont(ofSize: 32)
        titleLabel.textAlignment = .center
        titleLabel.translatesAutoresizingMaskIntoConstraints = false

        view.addSubview(titleLabel)

        // Configure login button
        let loginButton = UIButton(type: .system)
        loginButton.setTitle("Login", for: .normal)
        loginButton.titleLabel?.font = UIFont.systemFont(ofSize: 20, weight: .bold)
        loginButton.backgroundColor = .systemBlue
        loginButton.setTitleColor(.white, for: .normal)
        loginButton.layer.cornerRadius = 10
        loginButton.addTarget(self, action: #selector(handleLogin), for: .touchUpInside)
        loginButton.translatesAutoresizingMaskIntoConstraints = false

        view.addSubview(loginButton)

        // Layout constraints
        NSLayoutConstraint.activate([
            titleLabel.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 40),
            titleLabel.centerXAnchor.constraint(equalTo: view.centerXAnchor),

            loginButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 40),
            loginButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -40),
            loginButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -60),
            loginButton.heightAnchor.constraint(equalToConstant: 60)
        ])
    }

    @objc func handleLogin() {
        guard let authURL = URL(string: "https://snapfind.p-e.kr/oauth2/authorization/google") else { return }

        // snapfind:// 스킴을 사용하여 콜백 처리
        let session = ASWebAuthenticationSession(url: authURL, callbackURLScheme: "snapfind") { callbackURL, error in
            if let error = error {
                print("Authentication error: \(error.localizedDescription)")
                return
            }

            guard let callbackURL = callbackURL else { return }
            print("OAuth callback received: \(callbackURL.absoluteString)")
            
            // URL에서 이메일 파라미터 추출
            if let components = URLComponents(url: callbackURL, resolvingAgainstBaseURL: false),
               let email = components.queryItems?.first(where: { $0.name == "email" })?.value {
                print("Received email: \(email)")
                
                // 로그인 상태 저장
                UserDefaults.standard.set(true, forKey: "isLoggedIn")
                UserDefaults.standard.set(email, forKey: "userEmail")
                
                // 메인 화면으로 이동
                DispatchQueue.main.async {
                    let tabBarController = MainTabBarController()
                    self.view.window?.rootViewController = tabBarController
                    self.view.window?.makeKeyAndVisible()
                }
            }
        }

        session.presentationContextProvider = self
        session.start()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)

        let isLoggedIn = UserDefaults.standard.bool(forKey: "isLoggedIn")
        if isLoggedIn {
            let tabBarController = MainTabBarController()
            self.view.window?.rootViewController = tabBarController
            self.view.window?.makeKeyAndVisible()
        }
    }
}

extension LoginViewController: ASWebAuthenticationPresentationContextProviding {
    func presentationAnchor(for session: ASWebAuthenticationSession) -> ASPresentationAnchor {
        return self.view.window!
    }
}
