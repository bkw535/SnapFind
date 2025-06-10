//
//  ViewController.swift
//  ios-app
//
//  Created by 배건우 on 6/9/25.
//

import UIKit

import UIKit

class CameraViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {

    let imageView = UIImageView()

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        title = "Camera"

        imageView.contentMode = .scaleAspectFit
        imageView.frame = CGRect(x: 0, y: 100, width: view.frame.width, height: 300)
        view.addSubview(imageView)

        let button = UIButton(type: .system)
        button.setTitle("Open Camera", for: .normal)
        button.addTarget(self, action: #selector(openCamera), for: .touchUpInside)
        button.frame = CGRect(x: 20, y: 420, width: 200, height: 50)
        view.addSubview(button)
    }

    @objc func openCamera() {
        let picker = UIImagePickerController()
        picker.sourceType = .camera
        picker.delegate = self
        present(picker, animated: true)
    }

    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey: Any]) {
        if let image = info[.originalImage] as? UIImage {
            imageView.image = image
        }
        picker.dismiss(animated: true)
    }
}

