//
//  CustomTabBar.swift
//  ios-app
//
//  Created by 배건우 on 6/18/25.
//

import UIKit

class CustomTabBar: UITabBar {
    let customHeight: CGFloat = 100

    override func sizeThatFits(_ size: CGSize) -> CGSize {
        var size = super.sizeThatFits(size)
        size.height = customHeight
        return size
    }
}
