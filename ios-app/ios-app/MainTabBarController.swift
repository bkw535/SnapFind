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
        
        setValue(CustomTabBar(), forKey: "tabBar")

        let cameraNav = UINavigationController(rootViewController: CameraViewController())
        let resultNav = UINavigationController(rootViewController: CrawlingResultViewController())
        let profileNav = UINavigationController(rootViewController: ProfileViewController())

        cameraNav.tabBarItem = UITabBarItem(title: "Camera", image: UIImage(systemName: "camera"), tag: 0)
        resultNav.tabBarItem = UITabBarItem(title: "Results", image: UIImage(systemName: "doc.text.magnifyingglass"), tag: 1)
        profileNav.tabBarItem = UITabBarItem(title: "Profile", image: UIImage(systemName: "person.circle"), tag: 2)

        viewControllers = [cameraNav, resultNav, profileNav]
        tabBar.barTintColor = .white
        tabBar.backgroundColor = .white
        tabBar.isTranslucent = false
    }
}
