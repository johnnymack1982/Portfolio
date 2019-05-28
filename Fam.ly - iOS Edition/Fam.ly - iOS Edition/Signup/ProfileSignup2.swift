//
//  ProfileSignup2.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/26/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class ProfileSignup2: UIViewController, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var profilePhoto: UIImageView!
    
    
    
    // Class properties
    var galleryPicker: ImagePicker!
    var cameraPicker: UIImagePickerController!
    
    var mFirstName: String?
    var mDateOfBirth: Date?
    var mGenderId: Int?
    var mRoleId: Int?
    var mProfilePin: Int?
    
    var mIsParent = true;
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()

        galleryPicker = ImagePicker(presentationController: self, delegate: self)
        roundImageView()
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        cameraPicker.dismiss(animated: true, completion: nil)
        profilePhoto.image = info[.originalImage] as? UIImage
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if mIsParent {
            let parent = Parent(FirstName: mFirstName!, DateOfBirth: mDateOfBirth!, GenderID: mGenderId!, ProfilePIN: mProfilePin!, RoleID: mRoleId!)
            
            AccountUtils.addParent(Parent: parent, Photo: profilePhoto.image!)
        }
        
        else {
            let child = Child(FirstName: mFirstName!, DateOfBirth: mDateOfBirth!, GenderID: mGenderId!, ProfilePIN: mProfilePin!)
            
            AccountUtils.addChild(Child: child, Photo: profilePhoto.image!)
        }
    }
    
    
    
    // Custom methods
    func roundImageView() {
        profilePhoto.layer.borderWidth = 1.0
        profilePhoto.layer.masksToBounds = false
        profilePhoto.layer.borderColor = UIColor.white.cgColor
        profilePhoto.layer.cornerRadius = profilePhoto.frame.size.width / 2
        profilePhoto.clipsToBounds = true
    }
    
    func takePhoto() {
        cameraPicker =  UIImagePickerController()
        cameraPicker.delegate = self
        cameraPicker.sourceType = .camera
        
        present(cameraPicker, animated: true, completion: nil)
    }
    
    
    
    // Action methods
    @IBAction func captureImage(_ sender: UIButton) {
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



extension ProfileSignup2 : ImagePickerDelegate {
    
    func didSelect(image: UIImage?) {
        self.profilePhoto.image = image
        
        if image == nil {
            self.profilePhoto.image = UIImage(named: "Male Icon Large")
        }
    }
}
