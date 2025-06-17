//
//  CrawlingResultViewController.swift
//  ios-app
//
//  Created by 배건우 on 6/9/25.
//

import UIKit

struct Product {
    let name: String
    let price: String
    let shopUrl: String
    let shop: String

    init?(dict: [String: Any]) {
        guard let name = dict["name"] as? String,
              let price = dict["price"] as? String,
              let shopUrl = dict["shopUrl"] as? String,
              let shop = dict["shop"] as? String else { return nil }
        self.name = name
        self.price = price
        self.shopUrl = shopUrl
        self.shop = shop
    }
}

class CrawlingResultViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {

    var keyword: String?
    var products: [Product] = []

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        title = keyword ?? "검색 결과"

        let tableView = UITableView(frame: view.bounds)
        tableView.dataSource = self
        tableView.delegate = self
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: "ProductCell")
        view.addSubview(tableView)
    }

    // MARK: - UITableViewDataSource

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return products.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ProductCell", for: indexPath)
        let product = products[indexPath.row]
        cell.textLabel?.text = "\(product.name) (\(product.price))"
        cell.accessoryType = .disclosureIndicator
        return cell
    }

    // MARK: - UITableViewDelegate

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let product = products[indexPath.row]
        if let url = URL(string: product.shopUrl) {
            UIApplication.shared.open(url)
        }
    }
}
