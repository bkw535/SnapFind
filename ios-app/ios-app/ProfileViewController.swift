//
//  MyPageViewController.swift
//  ios-app
//
//  Created by 배건우 on 6/9/25.
//

import Foundation
import UIKit

class ProfileViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        title = "My Profile"

        let label = UILabel()
        label.text = "Welcome to your profile!"
        label.textAlignment = .center
        label.frame = view.bounds
        view.addSubview(label)
    }
}
