//
//  CrawlingResultViewController.swift
//  ios-app
//
//  Created by 배건우 on 6/9/25.
//

import Foundation
import UIKit

class CrawlingResultViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        title = "Crawling Results"

        let label = UILabel()
        label.text = "Results will appear here"
        label.translatesAutoresizingMaskIntoConstraints = false

        view.addSubview(label)
        NSLayoutConstraint.activate([
            label.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            label.centerYAnchor.constraint(equalTo: view.centerYAnchor)
        ])
    }
}
