//
//  MasterSignup3Controller.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/25/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class MasterSignup3Controller: UIViewController, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var familyPhoto: UIImageView!
    
    
    
    // Class properties
    var galleryPicker: ImagePicker!
    var cameraPicker: UIImagePickerController!
    
    var mFamilyName: String?
    var mStreetAddress: String?
    var mPostalCode: Int?
    
    var mEmail: String?
    var mPassword: String?
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Reference image picker and round family photo view
        galleryPicker = ImagePicker(presentationController: self, delegate: self)
        roundImageView()
    }
    
    // Populate selected image
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        cameraPicker.dismiss(animated: true, completion: nil)
        familyPhoto.image = info[.originalImage] as? UIImage
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        // Save account to file before continuing to next activity
        let account = Account(FamilyName: mFamilyName!, StreetAddress: mStreetAddress!, PostalCode: mPostalCode!, MasterEmail: mEmail!, MasterPassword: mPassword!)
        
        AccountUtils.saveAccount(Account: account, Photo: familyPhoto.image!)
        AccountUtils.createAccount(Account: account, Photo: familyPhoto.image!)
    }
    
    
    
    // Custom methods
    
    // Custom method to get photo from camera
    func takePhoto() {
        cameraPicker =  UIImagePickerController()
        cameraPicker.delegate = self
        cameraPicker.sourceType = .camera
        
        present(cameraPicker, animated: true, completion: nil)
    }
    
    // Custom method to round photo view
    func roundImageView() {
        familyPhoto.layer.borderWidth = 1.0
        familyPhoto.layer.masksToBounds = false
        familyPhoto.layer.borderColor = UIColor.white.cgColor
        familyPhoto.layer.cornerRadius = familyPhoto.frame.size.width / 2
        familyPhoto.clipsToBounds = true
    }

    
    
    
    // Action methods
    @IBAction func captureImage(_ sender: UIButton) {
        switch sender.tag {
            
        // If user clicked camera button, launch camera
        case 0:
            takePhoto()
            
        // If user clicked gallery button, launch gallery picker
        case 1:
            self.galleryPicker.present(from: sender)
            
        default:
            print("Invalid option")
        }
    }
}



extension MasterSignup3Controller : ImagePickerDelegate {
    
    // Populate selected image in UI
    func didSelect(image: UIImage?) {
        self.familyPhoto.image = image
        
        if image == nil {
            self.familyPhoto.image = UIImage(named: "Family Icon Large")
        }
    }
}
