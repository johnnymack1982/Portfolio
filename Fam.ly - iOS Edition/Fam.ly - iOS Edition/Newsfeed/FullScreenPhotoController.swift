//
//  FullScreenPhotoController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 6/9/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class FullScreenPhotoController: UIViewController {
    
    
    
    // UI Outlets
    @IBOutlet weak var photoView: UIImageView!
    
    
    
    // Class properties
    var mPhoto: UIImage?
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()

        self.navigationController?.setNavigationBarHidden(false, animated: true)
        
        if mPhoto != nil {
            photoView.image = mPhoto
        }
    }
}
