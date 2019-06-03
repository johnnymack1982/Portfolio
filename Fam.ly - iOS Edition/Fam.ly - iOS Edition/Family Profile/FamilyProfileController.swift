//
//  FamilyProfileController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/28/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit
import FirebaseFirestore
import FirebaseAuth

class FamilyProfileController: UIViewController, UINavigationControllerDelegate, UIImagePickerControllerDelegate, UITableViewDataSource, UITableViewDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var familyPhoto: UIImageView!
    @IBOutlet weak var familyNameDisplay: UILabel!
    @IBOutlet weak var addressDisplay: UILabel!
    @IBOutlet weak var profilesTable: UITableView!
    
    
    
    // Class properties
    var galleryPicker: ImagePicker!
    var cameraPicker: UIImagePickerController!
    
    var mAccount: Account?
    
    var mParent: Parent?
    var mChild: Child?
    
    var mPosition = 0
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController!.isNavigationBarHidden = false
        
        galleryPicker = ImagePicker(presentationController: self, delegate: self)
        
        mAccount = AccountUtils.loadAccount()
        
        AccountUtils.loadAccountPhoto(FamilyPhoto: familyPhoto)
        roundImageView()
        
        populateFamily()
        
        profilesTable.dataSource = self
        profilesTable.delegate = self
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        cameraPicker.dismiss(animated: true, completion: nil)
        familyPhoto.image = info[.originalImage] as? UIImage
        
        AccountUtils.saveAccount(Account: mAccount!, Photo: familyPhoto.image!)
        AccountUtils.uploadFamilyPhoto(Photo: familyPhoto.image!)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        AccountUtils.saveAccount(Account: mAccount!, Photo: familyPhoto.image!)
        AccountUtils.uploadFamilyPhoto(Photo: familyPhoto.image!)
        
        if segue.destination is ProfileController {
            let destination = segue.destination as? ProfileController
            
            if mParent != nil {
                destination?.mParent = mParent
                destination?.mEditingSelf = false
                destination?.mPosition = mPosition
            }
            
            else if mChild != nil {
                destination?.mChild = mChild
                destination?.mEditingSelf = false
                destination?.mPosition = mPosition
            }
        }
    }
    
    
    
    // Tableview methods
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return (mAccount?.getParents().count)! + (mAccount?.getChildren().count)!
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = profilesTable.dequeueReusableCell(withIdentifier: "profileReuseIdentifier") as! ProfileListCell
        
        let parents = mAccount?.getParents()
        let children = mAccount?.getChildren()
        
        if indexPath.row < parents!.count {
            let parent = parents![indexPath.row]
            let profileId = parent.getProfileId()
            cell.profileNameDisplay.text = parent.getFirstName() + " " + (mAccount?.getFamilyName())!
            
            AccountUtils.loadProfilePhoto(ProfileId: profileId, ProfilePhoto: cell.profilePhoto)
        }
        
        else {
            let child = children![indexPath.row - parents!.count]
            let profileId = child.getProfileId()
            cell.profileNameDisplay.text = child.getFirstName() + " " + (mAccount?.getFamilyName())!
            
            AccountUtils.loadProfilePhoto(ProfileId: profileId, ProfilePhoto: cell.profilePhoto)
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let parents = mAccount?.getParents()
        let children = mAccount?.getChildren()
        
        if indexPath.row < parents!.count {
            mParent = parents![indexPath.row]
            mChild = nil
            
            mPosition = indexPath.row
            
            performSegue(withIdentifier: "FamilyListToProfile", sender: nil)
        }
        
        else {
            mChild = children![indexPath.row - parents!.count]
            mParent = nil
            
            mPosition = indexPath.row - parents!.count
            
            performSegue(withIdentifier: "FamilyListToProfile", sender: nil)
        }
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
    
    func populateFamily() {
        familyNameDisplay.text = (mAccount?.getFamilyName())! + " Family"
        addressDisplay.text = mAccount?.getFullAddress()
    }

    
    
    
    // Action methods
    @IBAction func buttonClicked(_ sender: UIButton) {
        switch sender.tag {
        case 0:
            takePhoto()
            
        case 1:
            self.galleryPicker.present(from: sender)
            
        case 2:
            let alert = UIAlertController(title: "Delete Account?", message: "Are you sure you want to delete your account ? All of your information will be lost and you won't be able to get it back.", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Delete", style: .destructive, handler: { (UIAlertAction) in
                let database = Firestore.firestore()
                database.collection("accounts").document(Auth.auth().currentUser!.uid).getDocument { (document, error) in
                    if let document = document, document.exists {
                        let password = document.get("masterPassword") as? String
                        
                        let credential = EmailAuthProvider.credential(withEmail: (Auth.auth().currentUser?.email)!, password: password!)
                        
                        Auth.auth().currentUser?.reauthenticateAndRetrieveData(with: credential, completion: { (result, error) in
                            if error == nil {
                                Auth.auth().currentUser?.delete { error in
                                    if let error = error {
                                        print("Error deleting account", error)
                                    }
                                        
                                    else {
                                        print("Account deleted")
                                        
                                        do {
                                            try Auth.auth().signOut()
                                            
                                            self.performSegue(withIdentifier: "FailyProfileToLogin", sender: nil)
                                        }
                                            
                                        catch {}
                                    }
                                }
                            }
                                
                            else {
                                print("Could not delete account: ", error!)
                            }
                        })
                    }
                        
                    else {
                        print("Document does not exist")
                    }
                }
            }))
            
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
            
            self.present(alert, animated: true)
            
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
