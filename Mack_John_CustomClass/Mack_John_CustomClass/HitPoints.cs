using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mack_John_CustomClass
{
    class HitPoints
    {

        //Declare member variables;
        int mMaximumHitPoints;
        int mMinimumHitPoints;
        int mCurrentHitPoints;
        string mCharacterName;
        string mCharacterClass;



        //Declare constructor method
        public HitPoints(int _maximumHitPoints, int _minimumHitPoints, int _currentHitPoints, string _CharacterName, string _characterClass)
        {

            mMaximumHitPoints = _maximumHitPoints;
            mMinimumHitPoints = _minimumHitPoints;
            mCurrentHitPoints = _currentHitPoints;
            mCharacterName = _CharacterName;
            mCharacterClass = _characterClass;

        }




        //Declare setter methods
        public void SetMaxHitPoints(int _maxHitPoints)
        {

            //Set the value of mMaxHitPoints
            this.mMaximumHitPoints = _maxHitPoints;

        }

        public void SetMinHitPoints(int _minHitPoints)
        {

            //Set the value of mMinHitPoints
            this.mMinimumHitPoints = _minHitPoints;

        }

        public void SetCurrentHitPoints(int _currentHitPoints)
        {

            //Set the value of mCurrentHitPoints
            this.mCurrentHitPoints = _currentHitPoints;

        }

        public void SetCharacterName(string _charName)
        {

            //Set the value of mCharName
            this.mCharacterName = _charName;

        }

        public void SetCharacterClass(string _charClass)
        {

            //Set the value of mCharClass
            this.mCharacterClass = _charClass.ToLower();

        }



        //Declare getter methods
        public int GetMaximumHitPoints()
        {

            //Return the value of mMaxHitPoints
            return mMaximumHitPoints;

        }

        public int GetMinimumHitPoints()
        {

            //Return the value of mMinHitPoints
            return mMinimumHitPoints;

        }

        public int GetCurrentHitPoints()
        {

            //Return the value of mCurrentHitPoints
            return mCurrentHitPoints;

        }

        public string GetCharacterName()
        {

            //Return the value of mCharName
            return mCharacterName;

        }

        public string GetCharacterClass()
        {

            //Return the value of mCharClass
            return mCharacterClass;

        }



        //Build character starting and max HP based on class selection
        public void BuildCharacter(string _charClass)
        {

            if(_charClass == "fighter")
            {
                mMaximumHitPoints = 500;
                mCurrentHitPoints = 250;
                mCharacterClass = "Fighter";
            }

            else if (_charClass == "mage")
            {
                mMaximumHitPoints = 250;
                mCurrentHitPoints = 125;
                mCharacterClass = "Mage";
            }

            else if(_charClass == "healer")
            {
                mMaximumHitPoints = 120;
                mCurrentHitPoints = 60;
                mCharacterClass = "Healer";
            }

        }

    }
}
