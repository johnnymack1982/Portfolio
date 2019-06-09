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

       postInput.text = mPost?.getPostMessage()
    }
    
    
    
    // Custom methods
    private func updatePost() {
        mPost?.setPostMessage(PostMessage: postInput.text!)
        mPosts![(mIndexPath?.row)!] = mPost!
        
        PostUtils.updatePost(Post: mPost!, Posts: mPosts!, Controller: self)
    }
    
    
    
    // Action methods
    @IBAction func buttonClicked(_ sender: UIButton) {
        switch sender.tag {
        case 0:
            performSegue(withIdentifier: "unwindToNewsfeed", sender: self)
            
        case 1:
            updatePost()
            
        default:
            print("Invalid button")
        }
    }
}
