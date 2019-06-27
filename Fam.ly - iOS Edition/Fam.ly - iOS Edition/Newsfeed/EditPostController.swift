//
//  EditPostController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 6/7/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class EditPostController: UIViewController {
    
    
    
    // UI Outlets
    @IBOutlet weak var postInput: UITextField!
    @IBOutlet weak var postButtonsView: UIView!
    
    
    
    // Class properties
    var mPosts: [Post]?
    var mPost: Post?
    var mIndexPath: IndexPath?
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()

        // Populate input field with message for selected post
       postInput.text = mPost?.getPostMessage()
    }
    
    
    
    // Custom methods
    // Custom method to update post
    private func updatePost() {
        
        // Update selected post with new input
        mPost?.setPostMessage(PostMessage: postInput.text!)
        mPosts![(mIndexPath?.row)!] = mPost!
        
        PostUtils.updatePost(Post: mPost!, Posts: mPosts!, Controller: self)
    }
    
    
    
    // Action methods
    @IBAction func buttonClicked(_ sender: UIButton) {
        switch sender.tag {
            
        // If user clicked cancel button, return to previous activity
        case 0:
            performSegue(withIdentifier: "unwindToNewsfeed", sender: self)
            
        // If user clicked update button, call custom method to update post
        case 1:
            updatePost()
            
        default:
            print("Invalid button")
        }
    }
}
