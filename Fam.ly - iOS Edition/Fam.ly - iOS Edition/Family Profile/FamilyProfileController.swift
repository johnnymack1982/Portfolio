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
        
        // Show top navigation bar
        self.navigationController!.isNavigationBarHidden = false
        
        // Reference gallery picker
        galleryPicker = ImagePicker(presentationController: self, delegate: self)
        
        // Load account
        mAccount = AccountUtils.loadAccount()
        
        // Load family photo and call custom method to round image view
        AccountUtils.loadAccountPhoto(FamilyPhoto: familyPhoto)
        roundImageView()
        
        // Call custom method to populate family details
        populateFamily()
        
        // Set tableview delegate and data source
        profilesTable.dataSource = self
        profilesTable.delegate = self
    }
    
    // Populate UI with image selected from gallery picker
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        cameraPicker.dismiss(animated: true, completion: nil)
        familyPhoto.image = info[.originalImage] as? UIImage
        
        AccountUtils.saveAccount(Account: mAccount!, Photo: familyPhoto.image!)
        AccountUtils.uploadFamilyPhoto(Photo: familyPhoto.image!)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        // Save account and family photo
        AccountUtils.saveAccount(Account: mAccount!, Photo: familyPhoto.image!)
        AccountUtils.uploadFamilyPhoto(Photo: familyPhoto.image!)
        
        // Send selected profile to destination
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
        
        // Return number of profiles on account
        return (mAccount?.getParents().count)! + (mAccount?.getChildren().count)!
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        // Reference profile cell
        let cell = profilesTable.dequeueReusableCell(withIdentifier: "profileReuseIdentifier") as! ProfileListCell
        
        // Load all profiles on account
        let parents = mAccount?.getParents()
        let children = mAccount?.getChildren()
        
        // If profile is a parent, get profile id and name
        if indexPath.row < parents!.count {
            let parent = parents![indexPath.row]
            let profileId = parent.getProfileId()
            cell.profileNameDisplay.text = parent.getFirstName() + " " + (mAccount?.getFamilyName())!
            
            AccountUtils.loadProfilePhoto(ProfileId: profileId, ProfilePhoto: cell.profilePhoto)
        }
        
        // If profile is a child, get profile id and name
        else {
            let child = children![indexPath.row - parents!.count]
            let profileId = child.getProfileId()
            cell.profileNameDisplay.text = child.getFirstName() + " " + (mAccount?.getFamilyName())!
            
            AccountUtils.loadProfilePhoto(ProfileId: profileId, ProfilePhoto: cell.profilePhoto)
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        // Load all profiles in account
        let parents = mAccount?.getParents()
        let children = mAccount?.getChildren()
        
        // Calculate selected profile and launch profile activity
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
    // Custom method to round image view
    func roundImageView() {
        familyPhoto.layer.borderWidth = 1.0
        familyPhoto.layer.masksToBounds = false
        familyPhoto.layer.borderColor = UIColor.white.cgColor
        familyPhoto.layer.cornerRadius = familyPhoto.frame.size.width / 2
        familyPhoto.clipsToBounds = true
    }
    
    // Custom method to get photo from camera
    func takePhoto() {
        cameraPicker =  UIImagePickerController()
        cameraPicker.delegate = self
        cameraPicker.sourceType = .camera
        
        present(cameraPicker, animated: true, completion: nil)
    }
    
    // Custom method to populate family details
    func populateFamily() {
        familyNameDisplay.text = (mAccount?.getFamilyName())! + " Family"
        addressDisplay.text = mAccount?.getFullAddress()
    }

    
    
    
    // Action methods
    @IBAction func buttonClicked(_ sender: UIButton) {
        switch sender.tag {
            
        // If user clicked camera button, call custom method to get photo from camera
        case 0:
            takePhoto()
            
        // If user clicked gallery button, get photo from gallery picker
        case 1:
            self.galleryPicker.present(from: sender)
            
        // If user clicked delete account button...
        case 2:
            
            // Display confirmation alert
            let alert = UIAlertController(title: "Delete Account?", message: "Are you sure you want to delete your account ? All of your information will be lost and you won't be able to get it back.", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "Delete", style: .destructive, handler: { (UIAlertAction) in
                
                // Reference database location and attempt to retrieve password
                let database = Firestore.firestore()
                database.collection("accounts").document(Auth.auth().currentUser!.uid).getDocument { (document, error) in
                    if let document = document, document.exists {
                        
                        // Attempt to reauthenticate user
                        let password = document.get("masterPassword") as? String
                        let credential = EmailAuthProvider.credential(withEmail: (Auth.auth().currentUser?.email)!, password: password!)
                        Auth.auth().currentUser?.reauthenticateAndRetrieveData(with: credential, completion: { (result, error) in
                            if error == nil {
                                
                                // If successful, attempt to delete account credentials
                                Auth.auth().currentUser?.delete { error in
                                    if let error = error {
                                        print("Error deleting account", error)
                                    }
                                        
                                    // If successful, log out and return to login activity
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
    
    // Populate UI with selected image
    func didSelect(image: UIImage?) {
        self.familyPhoto.image = image
        
        if image == nil {
            self.familyPhoto.image = UIImage(named: "Family Icon Large")
        }
        
        AccountUtils.saveAccount(Account: mAccount!, Photo: familyPhoto.image!)
        AccountUtils.uploadFamilyPhoto(Photo: familyPhoto.image!)
    }
}
