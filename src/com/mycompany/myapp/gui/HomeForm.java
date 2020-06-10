/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.components.ImageViewer;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.mycompany.myapp.MyApplication;

/**
 *
 * @author cyrine
 */
public class HomeForm extends Form {

    Form current;

    public HomeForm() {
        current = this;
        setTitle("Elite");
        Container c = new Container(BoxLayout.x());

        setLayout(BoxLayout.y());

        add(new Label("Bienvenu a Notre Jardin d'enfant Elite"));
        //System.out.println(MyApplication.user.getId());

        current.getToolbar().addCommandToOverflowMenu("Logout", null, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                MyApplication.user=null;
                SignInForm m=new SignInForm();
                m.show();
                        

            }
        });

        current.getToolbar().addMaterialCommandToSideMenu("      Profil", FontImage.MATERIAL_ACCOUNT_CIRCLE, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Profile profile = new Profile();
                profile.getF().show();            
                        
            }
        });

    }

    public Form getF() {
        return current;
    }
}
