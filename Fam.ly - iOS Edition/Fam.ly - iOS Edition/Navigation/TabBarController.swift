//
//  TabBarController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/22/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class TabBarController: UITabBarController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Populate bottom navigation bar
        let appearance = UITabBarItem.appearance(whenContainedInInstancesOf: [TabBarController.self])
        appearance.setTitleTextAttributes([NSAttributedString.Key.foregroundColor: UIColor.white], for: .normal)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        // Hide top navigation bar
        self.navigationController!.isNavigationBarHidden = true
    }
}
