//
//  ProfileController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/23/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit
import FirebaseAuth
import FirebaseFirestore

class ProfileController: UIViewController, UINavigationControllerDelegate, UIImagePickerControllerDelegate, UITableViewDataSource, UITableViewDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var familyButton: UIButton!
    @IBOutlet weak var deleteProfileButton: UIButton!
    @IBOutlet weak var profilePhoto: UIImageView!
    @IBOutlet weak var nameDisplay: UILabel!
    @IBOutlet weak var dateTimeDisplay: UILabel!
    @IBOutlet weak var lastKnownLocationDisplay: UILabel!
    @IBOutlet weak var timeline: UITableView!
    
    
    
    // Class properties
    var galleryPicker: ImagePicker!
    var cameraPicker: UIImagePickerController!
    
    var mAccount: Account?
    var mParent: Parent?
    var mChild: Child?
    
    var mPosts: [Post] = [Post]()
    
    var mEditingSelf = true
    
    var mPosition: Int?
    
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mPosts = [Post]()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        populate()
        
        timeline.delegate = self
        timeline.dataSource = self
        
        mPosts = [Post]()
        
        let database = Firestore.firestore()
        var profileId: String?
        
        if mParent != nil {
            profileId = mParent?.getProfileId()
        }
            
        else if mChild != nil {
            profileId = mChild?.getProfileId()
        }
        
        let sevenDaysAgo = Calendar.current.date(byAdding: .day, value: -7, to: Date())
        
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + profileId!).collection("posts").whereField("timeStamp", isGreaterThan: sevenDaysAgo!).addSnapshotListener { querySnapshot, error in
            guard let documents = querySnapshot?.documents else {
                print("Error fetching documents: \(error!)")
                return
            }
            
            for document in documents {
                let postMessage = document.get("postMessage") as? String
                let timeStamp = document.get("timeStamp") as? Date
                let posterId = document.get("posterId") as? String
                let posterName = document.get("posterName") as? String
                let hasImage = document.get("hasImage") as? Bool
                
                let post = Post(PostMessage: postMessage!, TimeStamp: timeStamp!, PosterId: posterId!, PosterName: posterName!, HasImage: hasImage!)
                
                self.mPosts.append(post)
                
                self.timeline.reloadData()
            }
            
            self.mPosts.reverse()
            
            let encodedObject = try? JSONEncoder().encode(self.mPosts)
            
            if let encodedObjectJsonString = String(data: encodedObject!, encoding: .utf8) {
                
                
                let fileName = PostUtils.getDocumentsDirectory().appendingPathComponent("posts.fam")
                
                do {
                    // Attempt to save JSON string to file
                    try encodedObjectJsonString.write(to: fileName, atomically: true, encoding: String.Encoding.utf8)
                }
                    
                catch {
                    print("Failed to write data to file")
                }
            }
        }
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        cameraPicker.dismiss(animated: true, completion: nil)
        profilePhoto.image = info[.originalImage] as? UIImage
        
        if mEditingSelf {
            AccountUtils.saveProfile(Parent: mParent, Child: mChild)
        }
        
        AccountUtils.uploadProfilePhoto(Parent: mParent, Child: mChild, Photo: profilePhoto.image!)
    }
    
    
    
    // Tableview methods
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return mPosts.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
    
    func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        return 600
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let post = mPosts[indexPath.row]
        
        var cell: NewsfeedCell?
        
        if post.hasImage {
            cell = timeline.dequeueReusableCell(withIdentifier: "newsfeedCell") as? NewsfeedCell
            
            cell!.postImage.image = nil
            PostUtils.loadPostImage(View: cell!.postImage, PostId: post.getPostId(), ImageHeight: cell!.postImageHeight)
        }
            
        else {
            cell = timeline.dequeueReusableCell(withIdentifier: "newsfeedCellNoImage") as? NewsfeedCell
        }
        
        cell!.posterNameDisplay.text = post.getPosterName() + " " + (mAccount?.getFamilyName())!
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "MMMM dd, yyyy @ hh:mm a"
        let dateString = dateFormatter.string(from: post.getTimeStamp())
        cell!.timestampDisplay.text = dateString
        
        cell!.postMessageDisplay.text = post.getPostMessage()
        
        cell!.profilePhoto.image = nil
        AccountUtils.loadProfilePhoto(ProfileId: post.getPosterId(), ProfilePhoto: cell!.profilePhoto)
        cell!.profilePhoto.layer.borderWidth = 1.0
        cell!.profilePhoto.layer.masksToBounds = false
        cell!.profilePhoto.layer.borderColor = UIColor.white.cgColor
        cell!.profilePhoto.layer.cornerRadius = cell!.profilePhoto.frame.size.width / 2
        cell!.profilePhoto.clipsToBounds = true
        
        let twoMinutesAgo = Calendar.current.date(byAdding: .minute, value: -1, to: Date())
        let fiveMinutesAgo = Calendar.current.date(byAdding: .minute, value: -5, to: Date())
        
        if post.getTimeStamp() < fiveMinutesAgo! {
            cell?.editButton.isHidden = true
        }
            
        else {
            cell?.editButton.isHidden = false
        }
        
        if post.getTimeStamp() < twoMinutesAgo! {
            cell?.deleteButton.isHidden = true
        }
            
        else {
            cell?.deleteButton.isHidden = false
        }
        
        return cell!
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
