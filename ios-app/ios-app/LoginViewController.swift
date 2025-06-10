//
//  LoginViewController.swift
//  ios-app
//
//  Created by 배건우 on 6/9/25.
//

import Foundation
import UIKit

class LoginViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white

        let loginButton = UIButton(type: .system)
        loginButton.setTitle("Login", for: .normal)
        loginButton.addTarget(self, action: #selector(handleLogin), for: .touchUpInside)
        loginButton.translatesAutoresizingMaskIntoConstraints = false

        view.addSubview(loginButton)
        NSLayoutConstraint.activate([
            loginButton.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            loginButton.centerYAnchor.constraint(equalTo: view.centerYAnchor)
        ])
    }

    @objc func handleLogin() {
        let tabBarVC = MainTabBarController()
        tabBarVC.modalPresentationStyle = .fullScreen
        present(tabBarVC, animated: true, completion: nil)
    }
}
