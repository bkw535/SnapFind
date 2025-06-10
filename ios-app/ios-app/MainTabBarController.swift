//
//  MainTabBarController.swift
//  ios-app
//
//  Created by 배건우 on 6/9/25.
//

import Foundation
import UIKit

class MainTabBarController: UITabBarController {

    override func viewDidLoad() {
        super.viewDidLoad()

        let cameraVC = UINavigationController(rootViewController: CameraViewController())
        cameraVC.tabBarItem = UITabBarItem(title: "Home", image: UIImage(systemName: "camera"), tag: 0)

        let myPageVC = UINavigationController(rootViewController: MyPageViewController())
        myPageVC.tabBarItem = UITabBarItem(title: "My Page", image: UIImage(systemName: "person.circle"), tag: 1)

        viewControllers = [cameraVC, myPageVC]
    }
}
