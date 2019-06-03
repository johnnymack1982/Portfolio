//
//  ProfileController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/23/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit
import FirebaseAuth

class ProfileController: UIViewController, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var familyButton: UIButton!
    @IBOutlet weak var deleteProfileButton: UIButton!
    @IBOutlet weak var profilePhoto: UIImageView!
    @IBOutlet weak var nameDisplay: UILabel!
    @IBOutlet weak var dateTimeDisplay: UILabel!
    @IBOutlet weak var lastKnownLocationDisplay: UILabel!
    
    
    
    // Class properties
    var galleryPicker: ImagePicker!
    var cameraPicker: UIImagePickerController!
    
    var mAccount: Account?
    var mParent: Parent?
    var mChild: Child?
    
    var mEditingSelf = true
    
    var mPosition: Int?
    
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        populate()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        populate()
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        cameraPicker.dismiss(animated: true, completion: nil)
        profilePhoto.image = info[.originalImage] as? UIImage
        
        if mEditingSelf {
            AccountUtils.saveProfile(Parent: mParent, Child: mChild)
        }
        
        AccountUtils.uploadProfilePhoto(Parent: mParent, Child: mChild, Photo: profilePhoto.image!)
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
    
    func populate() {
        if mParent == nil && mEditingSelf == true {
            mParent = AccountUtils.loadParent()
        }
        
        if mChild == nil && mEditingSelf == true {
            mChild = AccountUtils.loadChild()
        }
        
        if mParent?.getProfileId() == AccountUtils.loadParent()?.getProfileId() {
            mEditingSelf = true
        }
        
        var profileId: String?
        
        if mParent != nil {
            profileId = mParent?.getProfileId()
            familyButton.isHidden = false
            deleteProfileButton.isHidden = false
        }
            
        else if mChild != nil {
            profileId = mChild?.getProfileId()
            familyButton.isHidden = true
            deleteProfileButton.isHidden = true
            
            if mEditingSelf == false {
                familyButton.isHidden = false
                deleteProfileButton.isHidden = false
            }
        }
        
        galleryPicker = ImagePicker(presentationController: self, delegate: self)
        
        mAccount = AccountUtils.loadAccount()
        
        AccountUtils.loadProfilePhoto(ProfileId: profileId!, ProfilePhoto: profilePhoto)
        
        roundImageView()
        populateProfile()
    }
    
    func populateProfile() {
        if mParent != nil {
            nameDisplay.text = (mParent?.getFirstName())! + " " + (mAccount?.getFamilyName())!
        }
            
        else if mChild != nil {
            nameDisplay.text = (mChild?.getFirstName())! + " " + (mAccount?.getFamilyName())!
            
        }
    }
    
    
    
    // Action methods
    @IBAction func buttonClicked(_ sender: UIButton) {
        switch sender.tag {
        case 0:
            takePhoto()
            
        case 1:
            self.galleryPicker.present(from: sender)
            
        case 2:
            if mParent != nil {
                let alert = UIAlertController(title: "Delete Profile?", message: "Are you sure you want to delete " + (mParent?.getFirstName())! + "? You won't be able to undo this.", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "Delete", style: .destructive, handler: { (UIAlertAction) in
                    var parents = self.mAccount?.getParents()
                    
                    var profileId: String?
                    
                    if self.mPosition != nil {
                        let parent = parents![self.mPosition!]
                        profileId = parent.getProfileId()
                        parents?.remove(at: self.mPosition!)
                    }
                        
                    else {
                        profileId = self.mParent?.getProfileId()
                        
                        self.mPosition = 0
                        for parent in parents! {
                            if parent.getProfileId() == parents![self.mPosition!].getProfileId() {
                                parents?.remove(at: self.mPosition!)
                            }
                            
                            else {
                                self.mPosition! += 1
                                
                            }
                        }
                    }
                    
                    self.mAccount?.setParents(parents: parents!)
                    
                    AccountUtils.saveAccount(Account: self.mAccount!, Photo: self.profilePhoto.image)
                    AccountUtils.deleteProfile(ProfileId: profileId!)
                    
                    if self.mEditingSelf {
                        do {
                            try Auth.auth().signOut()
                        }
                            
                        catch {
                            print("Error logging out")
                        }
                        
                        self.performSegue(withIdentifier: "ProfileToLogin", sender: nil)
                    }
                        
                    else {
                        self.performSegue(withIdentifier: "ProfileToFamilyList", sender: nil)
                    }
                }))
                
                alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
                
                self.present(alert, animated: true)
            }
                
            else if mChild != nil {
                let alert = UIAlertController(title: "Delete profile?", message: "Are you sure you want to delete " + (mChild?.getFirstName())! + "? You won't be able to undo this.", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "Delete", style: .destructive, handler: { (UIAlertAction) in
                    var children = self.mAccount?.getChildren()
                    
                    let child = children![self.mPosition!]
                    let profileId = child.getProfileId()
                    
                    children?.remove(at: self.mPosition!)
                    self.mAccount?.setChildren(children: children!)
                    
                    AccountUtils.saveAccount(Account: self.mAccount!, Photo: self.profilePhoto.image)
                    AccountUtils.deleteProfile(ProfileId: profileId)
                    
                    self.performSegue(withIdentifier: "ProfileToFamilyList", sender: nil)
                }))
                
                alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
                
                self.present(alert, animated: true)
            }
            
        default:
            print("Invalid option")
        }
    }
}



extension ProfileController : ImagePickerDelegate {
    
    func didSelect(image: UIImage?) {
        self.profilePhoto.image = image
        
        if image == nil {
            self.profilePhoto.image = UIImage(named: "Male Icon Large")
        }
        
        AccountUtils.saveProfile(Parent: mParent, Child: mChild)
        AccountUtils.uploadProfilePhoto(Parent: mParent, Child: mChild, Photo: profilePhoto.image!)
    }
}
