//
//  NewsfeedController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/22/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

public class NewsfeedController: UIViewController, UITextFieldDelegate, UINavigationControllerDelegate, UIImagePickerControllerDelegate, UITableViewDataSource, UITableViewDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var cancelButton: UIButton!
    @IBOutlet weak var postButton: UIButton!
    
    @IBOutlet weak var photoPicker: UIView!
    @IBOutlet weak var cameraButton: UIButton!
    @IBOutlet weak var galleryButton: UIButton!
    
    @IBOutlet weak var postInput: UITextField!
    
    @IBOutlet weak var postImageView: UIView!
    @IBOutlet weak var postImage: UIImageView!
    @IBOutlet weak var postImageHeight: NSLayoutConstraint!
    
    @IBOutlet weak var postButtonsView: UIView!
    @IBOutlet weak var postButtonsHeight: NSLayoutConstraint!
    
    @IBOutlet weak var newsfeed: UITableView!
    
    
    
    
    // Class properties
    var galleryPicker: ImagePicker!
    var cameraPicker: UIImagePickerController!
    
    var mPosts: [Post]?
    var mPost: Post?
    var mIndexPath: IndexPath?
    var mPhoto: UIImage?
    
    var mAccount: Account?
    
    var mChild: Child?
    var mParent: Parent?
    
    @objc let refreshControl = UIRefreshControl()
    
    
    
    
    
    // System generated methods
    override public func viewDidLoad() {
        super.viewDidLoad()
        
        // Load accont and profile
        mAccount = AccountUtils.loadAccount()
        mChild = AccountUtils.loadChild()
        mParent = AccountUtils.loadParent()
        
        // Listen for newsfeed updates
        PostUtils.listenForNews()
        
        // If profile is a parent, listen for incoming permission requests
        if mParent != nil {
            PermissionRequestUtils.receiveRequests(Parent: mParent!)
        }
        
        // If profile is a child, listen for permission request responses
        else if mChild != nil {
            PermissionRequestUtils.receiveResponses(Child: mChild!)
        }
        
        // Load newsfeed and populate
        mPosts = PostUtils.loadNewsfeed()
        newsfeed.dataSource = self
        newsfeed.delegate = self
        newsfeed.reloadData()
        postImage.image = nil
        
        // Reference gallery picker
        galleryPicker = ImagePicker(presentationController: self, delegate: self)
        
        // Set input field delgate
        postInput.delegate = self
        
        // Listen for account updates
        AccountUtils.listenForUpdates()
        
        // Set default UI states
        postImageHeight.constant = 0
        postButtonsHeight.constant = 0
        photoPicker.isHidden = true
        
        cancelButton.isHidden = true
        postButton.isHidden = true
    }
    
    override public func viewDidAppear(_ animated: Bool) {
        
        // Listen for newsfeed updates and load current newsfeed
        PostUtils.listenForNews()
        mPosts = PostUtils.loadNewsfeed()
        
        // Populate newsfeed
        newsfeed.dataSource = self
        newsfeed.delegate = self
        newsfeed.reloadData()
        
        // Reference pull-down refresher
        if #available(iOS 10.0, *) {
            newsfeed.refreshControl = refreshControl
        } else {
            newsfeed.addSubview(refreshControl)
        }
        refreshControl.tintColor = UIColor.orange
        refreshControl.addTarget(self, action: #selector(refreshNewsfeedData(_:)), for: .valueChanged)
        
        // Reference gallery picker
        galleryPicker = ImagePicker(presentationController: self, delegate: self)
        
        // Set input field delegate
        postInput.delegate = self
        
        // Listen for account updates
        AccountUtils.listenForUpdates()
    }
    
    override public func viewWillAppear(_ animated: Bool) {
        
        // Set default UI states
        postImageHeight.constant = 0
        postButtonsHeight.constant = 0
        photoPicker.isHidden = true
        
        cancelButton.isHidden = true
        postButton.isHidden = true
    }
    
    public func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        
        // Populate UI with selected image
        cameraPicker.dismiss(animated: true, completion: nil)
        postImage.image = info[.originalImage] as? UIImage
        
        if postImage.image != nil {
            postImageHeight.constant = 300
            
            postButtonsHeight.constant = 50
            
            photoPicker.isHidden = true
            
            cancelButton.isHidden = false
            postButton.isHidden = false
        }
    }
    
    public func textFieldDidBeginEditing(_ textField: UITextField) {
        
        // When input field is active, show post and cancel buttons
        if textField.tag == 0 {
            postButtonsHeight.constant = 50
            
            cancelButton.isHidden = false
            postButton.isHidden = false
        }
    }
    
    public func textFieldDidEndEditing(_ textField: UITextField) {
        
        // When input field is not active, hide post and cancel buttons
        if textField.tag == 0 {
            postImageHeight.constant = 0
            postButtonsHeight.constant = 0
            photoPicker.isHidden = true
            
            cancelButton.isHidden = true
            postButton.isHidden = true
        }
    }
    
    public func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        let nextTag = textField.tag + 1
        
        // On 'enter,' move to next input field. If last field, dismiss keyboard
        if let nextResponder = textField.superview?.viewWithTag(nextTag) {
            nextResponder.becomeFirstResponder()
        } else {
            textField.resignFirstResponder()
        }
        
        return true
    }
    
    override public func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.destination is EditPostController {
            
            // Send selected post to destination if editing
            let destination = segue.destination as? EditPostController
            
            destination?.mPosts = mPosts
            destination?.mPost = mPost
            destination?.mIndexPath = mIndexPath
        }
        
        else if segue.destination is FullScreenPhotoController {
            
            // Send selected photo to destination if viewing photo full-screen
            let destination = segue.destination as? FullScreenPhotoController
            
            destination?.mPhoto = mPhoto
        }
    }
    
    
    
    // Tableview methods
    public func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        // Return number of posts
        return mPosts!.count
    }
    
    public func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        // Set dynamic cell heigh
        return UITableView.automaticDimension
    }
    
    public func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        
        // Set estimated cell height
        return 600
    }
    
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        // Reference current post
        let post = mPosts![indexPath.row]
        
        var cell: NewsfeedCell?
        
        // If post has image, display image cell
        if post.hasImage {
            cell = newsfeed.dequeueReusableCell(withIdentifier: "newsfeedCell") as? NewsfeedCell
            
            cell!.postImage.image = nil
            PostUtils.loadPostImage(View: cell!.postImage, PostId: post.getPostId(), ImageHeight: cell!.postImageHeight)
        }
        
        // If post has no image, display non-image cell
        else {
            cell = newsfeed.dequeueReusableCell(withIdentifier: "newsfeedCellNoImage") as? NewsfeedCell
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
        
        // Otherwise, hide edit button
        else {
            cell?.deleteButton.isHidden = false
        }
        
        return cell!
    }
    
    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        // If selected cell has an image, launch full-screen image activity
        let cell = tableView.cellForRow(at: indexPath) as? NewsfeedCell
        if cell?.postImage != nil {
            mPhoto = cell?.postImage.image
            
            performSegue(withIdentifier: "NewsfeedToFullScreenImage", sender: self)
        }
    }
    
    
    
    // Custom methods
    // Custom method to get profile ID for logged in profile
    func getPosterid() -> String {
        
        // Load profile
        let parent = AccountUtils.loadParent()
        let child = AccountUtils.loadChild()
        
        // Reference profile Id
        var posterId: String?
        
        if parent != nil {
            posterId = parent?.getProfileId()
        }
            
        else if child != nil {
            posterId = child?.getProfileId()
        }
        
        // Return profile Id
        return posterId!
    }
    
    // Custom method to get name for logged in profile
    func getPosterName() -> String {
        
        // Load account
        let parent = AccountUtils.loadParent()
        let child = AccountUtils.loadChild()
        
        // Reference profile name
        var posterName: String?
        
        if parent != nil {
            posterName = parent?.getFirstName()
        }
            
        else if child != nil {
            posterName = child?.getFirstName()
        }
        
        // Return profile name
        return posterName!
    }
    
    // Custom method to get image from camera
    func takePhoto() {
        cameraPicker =  UIImagePickerController()
        cameraPicker.delegate = self
        cameraPicker.sourceType = .camera
        
        present(cameraPicker, animated: true, completion: nil)
    }
    
    // Method to refressh newsfeed on pull-down
    @objc private func refreshNewsfeedData(_ sender: Any) {
        
        // Load current newsfeed
        mPosts = PostUtils.loadNewsfeed()
        
        // Refresh newsfeed display
        newsfeed.reloadData()
        refreshControl.endRefreshing()
    }
    
    
    
    // Action methods
    @IBAction func postButtonClicked(_ sender: UIButton) {
        switch sender.tag {
            
        // If user clicked cancel button, reset UI
        case 0:
            postInput.resignFirstResponder()
            
            postImageHeight.constant = 0
            postButtonsHeight.constant = 0
            photoPicker.isHidden = true
            
            cancelButton.isHidden = true
            postButton.isHidden = true
         
        // If user clicked post button...
        case 1:
            
            // If post has valid input...
            if (postInput.text != nil && postInput.text?.trimmingCharacters(in: .whitespacesAndNewlines) != "") || postImage.image != nil {
                
                // Reference poster information
                let posterId = getPosterid()
                let posterName = getPosterName()
                var postMessage: String?
                
                // If post has message, reference it
                if postInput.text != nil {
                    postMessage = postInput.text
                }
                    
                // Otherwise, post message is blank
                else {
                    postMessage = ""
                }
                
                // Indicate whether or not post has an image
                var hasImage = false
                
                if postImage.image != nil {
                    hasImage = true
                }
                
                else {
                    hasImage = false
                }
                
                // Create post
                let post = Post(PostMessage: postMessage!, TimeStamp: Date(), PosterId: posterId, PosterName: posterName, HasImage: hasImage)
                PostUtils.createPost(Post: post, Controller: self, Photo: postImage.image)
                
                // Reset UI states
                postInput.resignFirstResponder()
                
                postImageHeight.constant = 0
                postButtonsHeight.constant = 0
                photoPicker.isHidden = true
                
                cancelButton.isHidden = true
                postButton.isHidden = true
                
                postImage.image = nil
                postInput.text = nil
            }
            
        default:
            print("Invalid button clicked")
        }
    }
    
    @IBAction func imageButtonClicked(_ sender: UITapGestureRecognizer) {
        
        // Toggle image picker
        if photoPicker.isHidden {
            photoPicker.isHidden = false
        }
            
        else {
            photoPicker.isHidden = true
        }
    }
    
    @IBAction func getPostPhoto(_ sender: UIButton) {
        switch sender.tag {
            
        // If user clicked camera button, call custom method to get image from camera
        case 0:
            takePhoto()
           
        // If user clicked gallery button, call custom method to get image from gallery picker
        case 1:
            self.galleryPicker.present(from: sender)
            
        default:
            print("Invalid button clicked")
        }
    }
    
    @IBAction func editButtonClicked(_ sender: UIButton) {
        
        // Reference cell button was clicked in and get associated post
        let cell = sender.superview?.superview as? NewsfeedCell
        mIndexPath = newsfeed.indexPath(for: cell!)
        mPost = mPosts![(mIndexPath?.row)!]
        
        switch sender.tag {
            
        // If user clicked delete button, display confirmation alert
        case 0:
            let alert = UIAlertController(title: "Delete Post?", message: "Are you sure you want to delete this post?", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Delete", style: .destructive, handler: { (UIAlertAction) in
                
                // Delete post and refresh newsfeed
                PostUtils.deletePost(PostId: self.mPost!.getPostId(), PosterId: self.mPost!.getPosterId(), Controller: self)
                
                self.mPosts?.remove(at: self.mIndexPath!.row)
                self.newsfeed.reloadData()
            }))
            
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
            self.present(alert, animated: true)
            
        // If user clicked edit button, launch edit activity
        case 1:
            performSegue(withIdentifier: "NewsfeedToEditPost", sender: nil)
            
            
        default:
            print("Invalid button")
        }
    }
    
    @IBAction func unwindToNewsfeed(segue:UIStoryboardSegue) {
        
        // Reload newsfeed
        mPosts = PostUtils.loadNewsfeed()
        newsfeed.reloadData()
    }
    
    @IBAction func imageClicked(_ sender: UITapGestureRecognizer) {
        
        // Launch full-screen image activity
        let photoView = sender.view as? UIImageView
        mPhoto = photoView?.image
        
        performSegue(withIdentifier: "NewsfeedToFullScreenImage", sender: self)
    }
}



extension NewsfeedController : ImagePickerDelegate {
    
    // Populate UI with selected image
    public func didSelect(image: UIImage?) {
        self.postImage.image = image
        
        if image != nil {
            self.postImageHeight.constant = 300
            self.postButtonsHeight.constant = 50
            photoPicker.isHidden = true
            postImageView.isHidden = false
            
            postButtonsView.isHidden = false
            self.cancelButton.isHidden = false
            self.postButton.isHidden = false
        }
            
        else {
            self.postImageHeight.constant = 300
            self.postButtonsHeight.constant = 50
            photoPicker.isHidden = true
            
            self.cancelButton.isHidden = false
            self.postButton.isHidden = false
            self.postImage.image = nil
        }
    }
}
