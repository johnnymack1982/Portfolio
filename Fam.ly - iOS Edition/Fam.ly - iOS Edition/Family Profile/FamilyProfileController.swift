//
//  FamilyProfileController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/28/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class FamilyProfileController: UIViewController, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var familyPhoto: UIImageView!
    @IBOutlet weak var familyNameDisplay: UILabel!
    @IBOutlet weak var addressDisplay: UILabel!
    
    
    
    // Class properties
    var galleryPicker: ImagePicker!
    var cameraPicker: UIImagePickerController!
    
    var mAccount: Account?
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        galleryPicker = ImagePicker(presentationController: self, delegate: self)

        mAccount = AccountUtils.loadAccount()
        AccountUtils.loadAccountPhoto(FamilyPhoto: familyPhoto)
        roundImageView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController!.isNavigationBarHidden = false
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        cameraPicker.dismiss(animated: true, completion: nil)
        familyPhoto.image = info[.originalImage] as? UIImage
        
        AccountUtils.saveAccount(Account: mAccount!, Photo: familyPhoto.image!)
        AccountUtils.uploadFamilyPhoto(Photo: familyPhoto.image!)
    }
    
    
    
    // Custom methods
    func roundImageView() {
        familyPhoto.layer.borderWidth = 1.0
        familyPhoto.layer.masksToBounds = false
        familyPhoto.layer.borderColor = UIColor.white.cgColor
        familyPhoto.layer.cornerRadius = familyPhoto.frame.size.width / 2
        familyPhoto.clipsToBounds = true
    }
    
    func takePhoto() {
        cameraPicker =  UIImagePickerController()
        cameraPicker.delegate = self
        cameraPicker.sourceType = .camera
        
        present(cameraPicker, animated: true, completion: nil)
    }

    
    
    
    // Action methods
    @IBAction func buttonClicked(_ sender: UIButton) {
        switch sender.tag {
        case 0:
            takePhoto()
            
        case 1:
            self.galleryPicker.present(from: sender)
            
        default:
            print("Invalid option")
        }
    }
}



extension FamilyProfileController : ImagePickerDelegate {
    
    func didSelect(image: UIImage?) {
        self.familyPhoto.image = image
        
        if image == nil {
            self.familyPhoto.image = UIImage(named: "Family Icon Large")
        }
        
        AccountUtils.saveAccount(Account: mAccount!, Photo: familyPhoto.image!)
        AccountUtils.uploadFamilyPhoto(Photo: familyPhoto.image!)
    }
}
