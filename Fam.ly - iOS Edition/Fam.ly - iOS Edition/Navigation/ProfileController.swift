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
    @IBOutlet weak var logoutButton: UIButton!
    
    
    
    // Class properties
    var galleryPicker: ImagePicker!
    var cameraPicker: UIImagePickerController!
    
    var mAccount: Account?
    var mParent: Parent?
    var mChild: Child?
    
    var mPosts = [Post]()
    var mPost: Post?
    var mIndexPath: IndexPath?
    
    var mPhoto: UIImage?
    
    var mEditingSelf = true
    
    var mPosition: Int?
    
    let refreshControl = UIRefreshControl()
    
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mPosts = [Post]()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        // Call custom method to populate UI
        populate()
        
        // Set timeline delegate and data source
        timeline.delegate = self
        timeline.dataSource = self
        
        // Reference pull-down refresher
        if #available(iOS 10.0, *) {
            timeline.refreshControl = refreshControl
        } else {
            timeline.addSubview(refreshControl)
        }
        refreshControl.tintColor = UIColor.orange
        refreshControl.addTarget(self, action: #selector(refreshTimeline(_:)), for: .valueChanged)
        
        mPosts = [Post]()
        
        // Reference database
        let database = Firestore.firestore()
        
        // Get profile ID
        var profileId: String?
        
        if mParent != nil {
            profileId = mParent?.getProfileId()
        }
            
        else if mChild != nil {
            profileId = mChild?.getProfileId()
        }
        
        // Reference date secen days in the past
        let sevenDaysAgo = Calendar.current.date(byAdding: .day, value: -7, to: Date())
        
        // Reference database location and attempt to download
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + profileId!).collection("posts").whereField("timeStamp", isGreaterThan: sevenDaysAgo!).addSnapshotListener { querySnapshot, error in
            guard let documents = querySnapshot?.documents else {
                print("Error fetching documents: \(error!)")
                return
            }
            
            // Loop through posts
            for document in documents {
                
                // Reference post data
                let postMessage = document.get("postMessage") as? String
                let timeStamp = document.get("timeStamp") as? Date
                let posterId = document.get("posterId") as? String
                let posterName = document.get("posterName") as? String
                let hasImage = document.get("hasImage") as? Bool
                
                // Add post to list and refresh display
                let post = Post(PostMessage: postMessage!, TimeStamp: timeStamp!, PosterId: posterId!, PosterName: posterName!, HasImage: hasImage!)
                self.mPosts.append(post)
                self.timeline.reloadData()
            }
            
            // Sort posts by date
            self.mPosts = self.mPosts.sorted(by: {$0.getTimeStamp().compare($1.getTimeStamp()) == .orderedDescending})
            
            // Save posts to file
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
    
    // Custom method to get photo from gallery picker
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        cameraPicker.dismiss(animated: true, completion: nil)
        profilePhoto.image = info[.originalImage] as? UIImage
        
        if mEditingSelf {
            AccountUtils.saveProfile(Parent: mParent, Child: mChild)
        }
        
        AccountUtils.uploadProfilePhoto(Parent: mParent, Child: mChild, Photo: profilePhoto.image!)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        // If editing post, send selected post to edit controller
        if segue.destination is EditPostController {
            let destination = segue.destination as? EditPostController
            
            destination?.mPosts = mPosts
            destination?.mPost = mPost
            destination?.mIndexPath = mIndexPath
        }
        
        // If viewing image full-screen, send selected image to full-screen controller
        else if segue.destination is FullScreenPhotoController {
            let destination = segue.destination as? FullScreenPhotoController
            
            destination?.mPhoto = mPhoto
        }
    }
    
    
    
    // Tableview methods
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        // Return number of posts in list
        return mPosts.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        // Set dynamic cell height
        return UITableView.automaticDimension
    }
    
    func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        
        // Set estimated cell height
        return 600
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        // Reference curret post
        let post = mPosts[indexPath.row]
        
        var cell: NewsfeedCell?
        
        // If post has an image, display image cell
        if post.hasImage {
            cell = timeline.dequeueReusableCell(withIdentifier: "newsfeedCell") as? NewsfeedCell
            
            cell!.postImage.image = nil
            PostUtils.loadPostImage(View: cell!.postImage, PostId: post.getPostId(), ImageHeight: cell!.postImageHeight)
        }
            
        // If post has no image, display no-image cell
        else {
            cell = timeline.dequeueReusableCell(withIdentifier: "newsfeedCellNoImage") as? NewsfeedCell
        }
        
        // Display poster name
        cell!.posterNameDisplay.text = post.getPosterName() + " " + (mAccount?.getFamilyName())!
        
        // Display timestamp
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "MMMM dd, yyyy @ hh:mm a"
        let dateString = dateFormatter.string(from: post.getTimeStamp())
        cell!.timestampDisplay.text = dateString
        
        // Display post message
        cell!.postMessageDisplay.text = post.getPostMessage()
        
        // Display profile photo
        cell!.profilePhoto.image = nil
        AccountUtils.loadProfilePhoto(ProfileId: post.getPosterId(), ProfilePhoto: cell!.profilePhoto)
        cell!.profilePhoto.layer.borderWidth = 1.0
        cell!.profilePhoto.layer.masksToBounds = false
        cell!.profilePhoto.layer.borderColor = UIColor.white.cgColor
        cell!.profilePhoto.layer.cornerRadius = cell!.profilePhoto.frame.size.width / 2
        cell!.profilePhoto.clipsToBounds = true
        
        // Reference times two and five minutes in the past
        let twoMinutesAgo = Calendar.current.date(byAdding: .minute, value: -1, to: Date())
        let fiveMinutesAgo = Calendar.current.date(byAdding: .minute, value: -5, to: Date())
        
        // If post is less than five minutes old, show edit button
        if post.getTimeStamp() < fiveMinutesAgo! {
            cell?.editButton.isHidden = true
        }
            
        // Otherwise, hide edit button
        else {
            cell?.editButton.isHidden = false
        }
        
        // If post is less than two minutes old, show delete button
        if post.getTimeStamp() < twoMinutesAgo! {
            cell?.deleteButton.isHidden = true
        }
            
        // Otherwise, hide delete button
        else {
            cell?.deleteButton.isHidden = false
        }
        
        return cell!
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        // Reference selected cell
        let cell = tableView.cellForRow(at: indexPath) as? NewsfeedCell
        
        // If cell has an image, launch full-screen photo controller
        if cell?.postImage != nil {
            mPhoto = cell?.postImage.image
            
            performSegue(withIdentifier: "ProfileToFullScreenPhoto", sender: self)
        }
    }
    
    
    
    // Custom methods
    // Custom method to round image view
    func roundImageView() {
        profilePhoto.layer.borderWidth = 1.0
        profilePhoto.layer.masksToBounds = false
        profilePhoto.layer.borderColor = UIColor.white.cgColor
        profilePhoto.layer.cornerRadius = profilePhoto.frame.size.width / 2
        profilePhoto.clipsToBounds = true
    }
    
    // Custom method to get image from camera
    func takePhoto() {
        cameraPicker =  UIImagePickerController()
        cameraPicker.delegate = self
        cameraPicker.sourceType = .camera
        
        present(cameraPicker, animated: true, completion: nil)
    }
    
    // Custom method to populate UI
    func populate() {
        
        // If editing self, load profile from file
        if mParent == nil && mEditingSelf == true {
            mParent = AccountUtils.loadParent()
        }
        
        if mChild == nil && mEditingSelf == true {
            mChild = AccountUtils.loadChild()
        }
        
        // If received profile is same as loaded profile, indicate is editing self
        if mParent?.getProfileId() == AccountUtils.loadParent()?.getProfileId() {
            mEditingSelf = true
        }
        
        var profileId: String?
        
        // If profile is a parent, show family and delete buttons and get profile ID
        if mParent != nil {
            profileId = mParent?.getProfileId()
            familyButton.isHidden = false
            deleteProfileButton.isHidden = false
        }
            
        // If profile is a child, hide family and delete buttons and get profile ID
        else if mChild != nil {
            profileId = mChild?.getProfileId()
            familyButton.isHidden = true
            deleteProfileButton.isHidden = true
            
            if mEditingSelf == false {
                familyButton.isHidden = false
                deleteProfileButton.isHidden = false
            }
        }
        
        // Reference gallery picker
        galleryPicker = ImagePicker(presentationController: self, delegate: self)
        
        // Load account
        mAccount = AccountUtils.loadAccount()
        
        // Load profile photo
        AccountUtils.loadProfilePhoto(ProfileId: profileId!, ProfilePhoto: profilePhoto)
        
        // Call custom methods to round image view and populate profile in UI
        roundImageView()
        populateProfile()
    }
    
    // Custom method to populate profile in UI
    func populateProfile() {
        
        // If profile is a parent...
        if mParent != nil {
            
            // Displaay name
            nameDisplay.text = (mParent?.getFirstName())! + " " + (mAccount?.getFamilyName())!
            
            // If viewing own profile, show logout button
            if mParent?.getProfileId() == AccountUtils.loadParent()?.getProfileId() {
                logoutButton.isHidden = false
            }
            
            // Otherwise, hide logout button
            else {
                logoutButton.isHidden = true
            }
        }
            
        // If profile is a child, show logout button
        else if mChild != nil {
            nameDisplay.text = (mChild?.getFirstName())! + " " + (mAccount?.getFamilyName())!
            
            logoutButton.isHidden = false
        }
        
        // Update UI with current profile location
        let locationUtils = LocationUtils(Location: nil)
        locationUtils.updateLocationDisplay(Controller: self, TimeStampDisplay: dateTimeDisplay, LastKnowLocationDisplay: lastKnownLocationDisplay, Parent: mParent, Child: mChild)
    }
    
    @objc private func refreshTimeline(_ sender: Any) {
        mPosts = [Post]()
        
        // Reference databse
        let database = Firestore.firestore()
        var profileId: String?
        
        // Get profile ID
        if mParent != nil {
            profileId = mParent?.getProfileId()
        }
            
        else if mChild != nil {
            profileId = mChild?.getProfileId()
        }
        
        // Reference date seven days in the past
        let sevenDaysAgo = Calendar.current.date(byAdding: .day, value: -7, to: Date())
        
        // Reference database location and attempt to download
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + profileId!).collection("posts").whereField("timeStamp", isGreaterThan: sevenDaysAgo!).addSnapshotListener { querySnapshot, error in
            guard let documents = querySnapshot?.documents else {
                print("Error fetching documents: \(error!)")
                return
            }
            
            // Loop through posts
            for document in documents {
                
                // Reference post data
                let postMessage = document.get("postMessage") as? String
                let timeStamp = document.get("timeStamp") as? Date
                let posterId = document.get("posterId") as? String
                let posterName = document.get("posterName") as? String
                let hasImage = document.get("hasImage") as? Bool
                
                // Add post to list and refresh display
                let post = Post(PostMessage: postMessage!, TimeStamp: timeStamp!, PosterId: posterId!, PosterName: posterName!, HasImage: hasImage!)
                self.mPosts.append(post)
                self.timeline.reloadData()
                self.refreshControl.endRefreshing()
            }
            
            // Sort posts
            self.mPosts = self.mPosts.sorted(by: {$0.getTimeStamp().compare($1.getTimeStamp()) == .orderedDescending})
            
            // Save posts to file
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
    
    
    
    // Action methods
    @IBAction func buttonClicked(_ sender: UIButton) {
        switch sender.tag {
            
        // If user clicked camera button, call custom method to get image from camera
        case 0:
            takePhoto()
            
        // If user clicked gallery button, get image from gallery picker
        case 1:
            self.galleryPicker.present(from: sender)
            
        // If user clicked delete button...
        case 2:
            if mParent != nil {
                
                // Build confirmation alert
                let alert = UIAlertController(title: "Delete Profile?", message: "Are you sure you want to delete " + (mParent?.getFirstName())! + "? You won't be able to undo this.", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "Delete", style: .destructive, handler: { (UIAlertAction) in
                    
                    // Get parent profiles
                    var parents = self.mAccount?.getParents()
                    
                    var profileId: String?
                    
                    // If profile is a parent, remove it from the account
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
                    
                    // If deleting logged in profile, log out and return to login activity
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
                
            // If deleting child, show confirmation alert and remove profile from account if user confirms
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
            
         // If user clicked logout button, log out and return to login activity
        case 3:
                let alert = UIAlertController(title: "Log Out?", message: "Are you sure you want to log this profile out?", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "Log Out", style: .destructive, handler: { action in
                    do {
                    try Auth.auth().signOut()
                    
                    self.performSegue(withIdentifier: "ProfileToLogin", sender: nil)
                    }
                    
                    catch {
                        let alert = UIAlertController(title: "Uh Oh!", message: "There was a problem logging out. Please try again.", preferredStyle: .alert)
                        
                        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                        
                        self.present(alert, animated: true)
                    }
                }))
                
                alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
                
                self.present(alert, animated: true)
            
        default:
            print("Invalid option")
        }
    }
    
    @IBAction func editButtonClicked(_ sender: UIButton) {
        
        // Reference cell the button was clicked in and the associated post
        let cell = sender.superview?.superview as? NewsfeedCell
        mIndexPath = timeline.indexPath(for: cell!)
        mPost = mPosts[(mIndexPath?.row)!]
        
        switch sender.tag {
            
        // If user clicked delete post, display confirmation alert and remove if user confirms
        case 0:
            let alert = UIAlertController(title: "Delete Post?", message: "Are you sure you want to delete this post?", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Delete", style: .destructive, handler: { (UIAlertAction) in
                PostUtils.deletePost(PostId: self.mPost!.getPostId(), PosterId: self.mPost!.getPosterId(), Controller: self)
                
                self.mPosts.remove(at: self.mIndexPath!.row)
                self.timeline.reloadData()
            }))
            
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
            
            self.present(alert, animated: true)
            
            
        // If user clicked edit button, launch edit activity
        case 1:
            performSegue(withIdentifier: "ProfileToEditPost", sender: nil)
            
            
        default:
            print("Invalid button")
        }
    }
    
    @IBAction func unwindToProfile(segue:UIStoryboardSegue) {
        
        // Reload timeline
        mPosts = PostUtils.loadNewsfeed()
        
        timeline.reloadData()
    }
}



extension ProfileController : ImagePickerDelegate {
    
    // Display selected image in UI
    func didSelect(image: UIImage?) {
        self.profilePhoto.image = image
        
        if image == nil {
            self.profilePhoto.image = UIImage(named: "Male Icon Large")
        }
        
        AccountUtils.saveProfile(Parent: mParent, Child: mChild)
        AccountUtils.uploadProfilePhoto(Parent: mParent, Child: mChild, Photo: profilePhoto.image!)
    }
}
