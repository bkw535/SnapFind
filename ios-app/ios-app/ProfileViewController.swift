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
        guard let email = UserDefaults.standard.string(forKey: "userEmail") else {
            print("이메일 정보 없음")
            return
        }
        guard let baseURL = Bundle.main.object(forInfoDictionaryKey: "API_BASE_URL") as? String else {
            print("API_BASE_URL not found")
            return
        }

        let userInfoURL = URL(string: "\(baseURL)/api/users/me?email=\(email)")!
        var userInfoRequest = URLRequest(url: userInfoURL)
        userInfoRequest.httpMethod = "GET"

        let userInfoTask = URLSession.shared.dataTask(with: userInfoRequest) { [weak self] data, response, error in
            guard let self = self else { return }
            if let error = error {
                print("사용자 정보 조회 실패: \(error.localizedDescription)")
                return
            }
            guard let data = data else {
                print("데이터 없음")
                return
            }

            if let responseString = String(data: data, encoding: .utf8) {
                print("사용자 정보 응답: \(responseString)")
            }

            do {
                let userJson = try JSONSerialization.jsonObject(with: data) as? [String: Any]
                guard let userId = userJson?["id"] as? Int64 else {
                    print("사용자 ID 파싱 실패")
                    return
                }

                self.fetchHistoryWithUserId(userId: userId)
            } catch {
                print("사용자 정보 파싱 실패: \(error.localizedDescription)")
            }
        }
        userInfoTask.resume()
    }

    private func fetchHistoryWithUserId(userId: Int64) {
        guard let baseURL = Bundle.main.object(forInfoDictionaryKey: "API_BASE_URL") as? String else {
            print("API_BASE_URL not found")
            return
        }
        let url = URL(string: "\(baseURL)/api/users/history?userId=\(userId)")!
        let task = URLSession.shared.dataTask(with: url) { [weak self] data, response, error in
            guard let self = self, let data = data, error == nil else { return }

            if let responseString = String(data: data, encoding: .utf8) {
                print("서버 응답: \(responseString)")
            }

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

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return searchHistory.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "HistoryCell", for: indexPath)
        cell.textLabel?.text = searchHistory[indexPath.row]
        return cell
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        print("Selected: \(searchHistory[indexPath.row])")
    }
}
