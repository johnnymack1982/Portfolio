//
//  NewsfeedCell.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 6/4/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class NewsfeedCell: UITableViewCell {
    
    
    
    // UI Outlets
    @IBOutlet weak var posterNameDisplay: UILabel!
    @IBOutlet weak var timestampDisplay: UILabel!
    @IBOutlet weak var postMessageDisplay: UITextView!
    @IBOutlet weak var profilePhoto: UIImageView!
    @IBOutlet weak var postImage: UIImageView!
    @IBOutlet weak var postImageHeight: NSLayoutConstraint!
    @IBOutlet weak var editButton: UIButton!
    @IBOutlet weak var deleteButton: UIButton!
    
    
    
    
    
    // System generated methods
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
}
