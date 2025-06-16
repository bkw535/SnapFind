//
//  MyPageViewController.swift
//  ios-app
//
//  Created by 배건우 on 6/9/25.
//

import Foundation
import UIKit

class ProfileViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    private let tableView = UITableView()
    private var searchHistory: [String] = []

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        title = "My Profile"
        setupTableView()
        fetchSearchHistory()
    }

    private func setupTableView() {
        tableView.frame = view.bounds
        tableView.dataSource = self
        tableView.delegate = self
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: "HistoryCell")
        tableView.rowHeight = 50
        tableView.showsVerticalScrollIndicator = true
        view.addSubview(tableView)
    }

    private func fetchSearchHistory() {
        guard let url = URL(string: "https://snapfind.p-e.kr/api/user/history?userId=1") else { return }

        let task = URLSession.shared.dataTask(with: url) { [weak self] data, response, error in
            guard let self = self, let data = data, error == nil else { return }

            do {
                let history = try JSONDecoder().decode([String].self, from: data)
                DispatchQueue.main.async {
                    self.searchHistory = history
                    self.tableView.reloadData()
                }
            } catch {
                print("Failed to decode search history: \(error)")
            }
        }

        task.resume()
    }

    // MARK: - UITableViewDataSource

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return searchHistory.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "HistoryCell", for: indexPath)
        cell.textLabel?.text = searchHistory[indexPath.row]
        return cell
    }

    // MARK: - UITableViewDelegate

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        print("Selected: \(searchHistory[indexPath.row])")
    }
}
