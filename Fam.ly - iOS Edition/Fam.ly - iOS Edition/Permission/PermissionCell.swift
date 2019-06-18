//
//  ChildPermissionCell.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 6/12/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class PermissionCell: UITableViewCell {
    
    
    
    // UI Outlets
    @IBOutlet weak var profilePhoto: UIImageView!
    @IBOutlet weak var profileNameDisplay: UILabel!
    @IBOutlet weak var timeStampDisplay: UILabel!
    @IBOutlet weak var requestMessageDisplay: UITextView!
    @IBOutlet weak var requestStatusDisplay: UIImageView!
    @IBOutlet weak var grantButton: UIButton!
    @IBOutlet weak var denybutton: UIButton!
    
    
    
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
