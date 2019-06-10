//
//  NewsfeedController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/22/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class NewsfeedController: UIViewController, UITextFieldDelegate, UINavigationControllerDelegate, UIImagePickerControllerDelegate, UITableViewDataSource, UITableViewDelegate {
    
    
    
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
    
    
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mAccount = AccountUtils.loadAccount()
        
        PostUtils.listenForNews()
        mPosts = PostUtils.loadNewsfeed()
        
        newsfeed.dataSource = self
        newsfeed.delegate = self
        newsfeed.reloadData()
        
        postImage.image = nil
        
        galleryPicker = ImagePicker(presentationController: self, delegate: self)
        
        postInput.delegate = self
        
        AccountUtils.listenForUpdates()
        
        postImageHeight.constant = 0
        postButtonsHeight.constant = 0
        photoPicker.isHidden = true
        
        cancelButton.isHidden = true
        postButton.isHidden = true
    }
    
    override func viewDidAppear(_ animated: Bool) {
        PostUtils.listenForNews()
        mPosts = PostUtils.loadNewsfeed()
        
        newsfeed.dataSource = self
        newsfeed.delegate = self
        newsfeed.reloadData()
        
        galleryPicker = ImagePicker(presentationController: self, delegate: self)
        
        postInput.delegate = self
        
        AccountUtils.listenForUpdates()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        postImageHeight.constant = 0
        postButtonsHeight.constant = 0
        photoPicker.isHidden = true
        
        cancelButton.isHidden = true
        postButton.isHidden = true
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
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
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        if textField.tag == 0 {
            postButtonsHeight.constant = 50
            
            cancelButton.isHidden = false
            postButton.isHidden = false
        }
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField.tag == 0 {
            postImageHeight.constant = 0
            postButtonsHeight.constant = 0
            photoPicker.isHidden = true
            
            cancelButton.isHidden = true
            postButton.isHidden = true
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        let nextTag = textField.tag + 1
        
        if let nextResponder = textField.superview?.viewWithTag(nextTag) {
            nextResponder.becomeFirstResponder()
        } else {
            textField.resignFirstResponder()
        }
        
        return true
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.destination is EditPostController {
            let destination = segue.destination as? EditPostController
            
            destination?.mPosts = mPosts
            destination?.mPost = mPost
            destination?.mIndexPath = mIndexPath
        }
        
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
        return mPosts!.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
    
    func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        return 600
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let post = mPosts![indexPath.row]
        
        var cell: NewsfeedCell?
        
        if post.hasImage {
            cell = newsfeed.dequeueReusableCell(withIdentifier: "newsfeedCell") as? NewsfeedCell
            
            cell!.postImage.image = nil
            PostUtils.loadPostImage(View: cell!.postImage, PostId: post.getPostId(), ImageHeight: cell!.postImageHeight)
        }
        
        else {
            cell = newsfeed.dequeueReusableCell(withIdentifier: "newsfeedCellNoImage") as? NewsfeedCell
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
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let cell = tableView.cellForRow(at: indexPath) as? NewsfeedCell
        mPhoto = cell?.postImage.image
        
        performSegue(withIdentifier: "NewsfeedToFullScreenImage", sender: self)
    }
    
    
    
    // Custom methods
    func getPosterid() -> String {
        let parent = AccountUtils.loadParent()
        let child = AccountUtils.loadChild()
        
        var posterId: String?
        
        if parent != nil {
            posterId = parent?.getProfileId()
        }
            
        else if child != nil {
            posterId = child?.getProfileId()
        }
        
        return posterId!
    }
    
    func getPosterName() -> String {
        let parent = AccountUtils.loadParent()
        let child = AccountUtils.loadChild()
        
        var posterName: String?
        
        if parent != nil {
            posterName = parent?.getFirstName()
        }
            
        else if child != nil {
            posterName = child?.getFirstName()
        }
        
        return posterName!
    }
    
    func takePhoto() {
        cameraPicker =  UIImagePickerController()
        cameraPicker.delegate = self
        cameraPicker.sourceType = .camera
        
        present(cameraPicker, animated: true, completion: nil)
    }
    
    
    
    // Action methods
    @IBAction func postButtonClicked(_ sender: UIButton) {
        switch sender.tag {
        case 0:
            postInput.resignFirstResponder()
            
            postImageHeight.constant = 0
            postButtonsHeight.constant = 0
            photoPicker.isHidden = true
            
            cancelButton.isHidden = true
            postButton.isHidden = true
            
        case 1:
            if (postInput.text != nil && postInput.text?.trimmingCharacters(in: .whitespacesAndNewlines) != "") || postImage.image != nil {
                let posterId = getPosterid()
                let posterName = getPosterName()
                var postMessage: String?
                
                if postInput.text != nil {
                    postMessage = postInput.text
                }
                    
                else {
                    postMessage = ""
                }
                
                var hasImage = false
                
                if postImage.image != nil {
                    hasImage = true
                }
                
                else {
                    hasImage = false
                }
                
                let post = Post(PostMessage: postMessage!, TimeStamp: Date(), PosterId: posterId, PosterName: posterName, HasImage: hasImage)
                
                PostUtils.createPost(Post: post, Controller: self, Photo: postImage.image)
                
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
        if photoPicker.isHidden {
            photoPicker.isHidden = false
        }
            
        else {
            photoPicker.isHidden = true
        }
    }
    
    @IBAction func getPostPhoto(_ sender: UIButton) {
        switch sender.tag {
        case 0:
            takePhoto()
            
        case 1:
            self.galleryPicker.present(from: sender)
            
        default:
            print("Invalid button clicked")
        }
    }
    
    @IBAction func editButtonClicked(_ sender: UIButton) {
        let cell = sender.superview?.superview as? NewsfeedCell
        mIndexPath = newsfeed.indexPath(for: cell!)
        mPost = mPosts![(mIndexPath?.row)!]
        
        switch sender.tag {
        case 0:
            let alert = UIAlertController(title: "Delete Post?", message: "Are you sure you want to delete this post?", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Delete", style: .destructive, handler: { (UIAlertAction) in
                PostUtils.deletePost(PostId: self.mPost!.getPostId(), PosterId: self.mPost!.getPosterId(), Controller: self)
                
                self.mPosts?.remove(at: self.mIndexPath!.row)
                self.newsfeed.reloadData()
            }))
            
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
            
            self.present(alert, animated: true)
            
            
        case 1:
            performSegue(withIdentifier: "NewsfeedToEditPost", sender: nil)
            
            
        default:
            print("Invalid button")
        }
    }
    
    @IBAction func unwindToNewsfeed(segue:UIStoryboardSegue) {
        mPosts = PostUtils.loadNewsfeed()
        
        newsfeed.reloadData()
    }
    
    @IBAction func imageClicked(_ sender: UITapGestureRecognizer) {
        let photoView = sender.view as? UIImageView
        
        mPhoto = photoView?.image
        
        performSegue(withIdentifier: "NewsfeedToFullScreenImage", sender: self)
    }
}



extension NewsfeedController : ImagePickerDelegate {
    
    func didSelect(image: UIImage?) {
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
