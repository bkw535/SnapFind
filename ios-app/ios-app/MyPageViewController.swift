//
//  MyPageViewController.swift
//  ios-app
//
//  Created by 배건우 on 6/9/25.
//

import Foundation
import UIKit

class MyPageViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .systemGray6
        title = "My Page"

        let label = UILabel()
        label.text = "Welcome to My Page"
        label.translatesAutoresizingMaskIntoConstraints = false

        view.addSubview(label)
        NSLayoutConstraint.activate([
            label.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            label.centerYAnchor.constraint(equalTo: view.centerYAnchor)
        ])
    }
}
