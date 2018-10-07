//
//  EventCell.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/2/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import UIKit

class EventCell: UITableViewCell {
    
    
    
    // MARK: - UI Outlets
    @IBOutlet weak var eventImage: UIImageView!
    @IBOutlet weak var taskCompletionIndicator: UIImageView!
    @IBOutlet weak var eventNameLabel: UILabel!
    @IBOutlet weak var eventTimeLabel: UILabel!
    
    
    
    
    // MARK: - System Generated Functions
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
}
