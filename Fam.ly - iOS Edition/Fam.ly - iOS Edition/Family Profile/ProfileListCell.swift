//
//  ProfileListCell.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/29/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class ProfileListCell: UITableViewCell {
    
    
    
    // UI Outlets
    @IBOutlet weak var profilePhoto: UIImageView!
    @IBOutlet weak var profileNameDisplay: UILabel!
    
    
    
    // Class properties
    
    
    
    // System generated methods
    override func awakeFromNib() {
        super.awakeFromNib()
        roundImageView()
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    
    
    // Custom methods
    func roundImageView() {
        profilePhoto.layer.borderWidth = 1.0
        profilePhoto.layer.masksToBounds = false
        profilePhoto.layer.borderColor = UIColor.white.cgColor
        profilePhoto.layer.cornerRadius = profilePhoto.frame.size.width / 2
        profilePhoto.clipsToBounds = true
    }
}
