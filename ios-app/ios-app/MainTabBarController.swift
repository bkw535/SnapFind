//
//  MainTabBarController.swift
//  ios-app
//
//  Created by 배건우 on 6/9/25.
//

import Foundation
//import UIKit
//
//class MainTabBarController: UITabBarController {
//
//    override func viewDidLoad() {
//        super.viewDidLoad()
//
//        let cameraVC = UINavigationController(rootViewController: CameraViewController())
//        cameraVC.tabBarItem = UITabBarItem(title: "Home", image: UIImage(systemName: "camera"), tag: 0)
//
//        let myPageVC = UINavigationController(rootViewController: MyPageViewController())
//        myPageVC.tabBarItem = UITabBarItem(title: "My Page", image: UIImage(systemName: "person.circle"), tag: 1)
//
//        viewControllers = [cameraVC, myPageVC]
//    }
//}
import UIKit

class MainTabBarController: UITabBarController {

    override func viewDidLoad() {
        super.viewDidLoad()

        let cameraVC = CameraViewController()
        let dummyVC1 = CrawlingResultViewController()
        let dummyVC2 = ProfileViewController()

        cameraVC.tabBarItem = UITabBarItem(title: "", image: UIImage(systemName: "camera.circle"), tag: 0)
        dummyVC1.tabBarItem = UITabBarItem(title: "Results", image: UIImage(systemName: "doc.text.magnifyingglass"), tag: 1)
        dummyVC2.tabBarItem = UITabBarItem(title: "Profile", image: UIImage(systemName: "person.circle"), tag: 2)

        viewControllers = [cameraVC, dummyVC1, dummyVC2]
    }
}
