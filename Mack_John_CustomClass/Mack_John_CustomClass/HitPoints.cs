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
        int mMaxHitPoints;
        int mMinHitPoints;
        int mCurrentHitPoints;
        string mCharName;
        string mCharClass;



        //Declare constructor method
        public HitPoints(int _maxHitPoints, int _minHitPoints, int _currentHitPoints, string _CharName, string _charClass)
        {

            mMaxHitPoints = _maxHitPoints;
            mMinHitPoints = _minHitPoints;
            mCurrentHitPoints = _currentHitPoints;
            mCharName = _CharName;
            mCharClass = _charClass;

        }




        //Declare setter methods
        public void SetMaxHitPoints(int _maxHitPoints)
        {

            //Set the value of mMaxHitPoints
            this.mMaxHitPoints = _maxHitPoints;

        }

        public void SetMinHitPoints(int _minHitPoints)
        {

            //Set the value of mMinHitPoints
            this.mMinHitPoints = _minHitPoints;

        }

        public void SetCurrentHitPoints(int _currentHitPoints)
        {

            //Set the value of mCurrentHitPoints
            this.mCurrentHitPoints = _currentHitPoints;

        }

        public void SetCharName(string _charName)
        {

            //Set the value of mCharName
            this.mCharName = _charName;

        }

        public void SetCharClass(string _charClass)
        {

            //Set the value of mCharClass
            this.mCharClass = _charClass.ToLower();

        }



        //Declare getter methods
        public int GetMaxHitPoints()
        {

            //Return the value of mMaxHitPoints
            return mMaxHitPoints;

        }

        public int GetMinHitPoints()
        {

            //Return the value of mMinHitPoints
            return mMinHitPoints;

        }

        public int GetCurrentHitPoints()
        {

            //Return the value of mCurrentHitPoints
            return mCurrentHitPoints;

        }

        public string GetCharName()
        {

            //Return the value of mCharName
            return mCharName;

        }

        public string GetCharClass()
        {

            //Return the value of mCharClass
            return mCharClass;

        }



        //Build character starting and max HP based on class selection
        public void BuildCharacter(string _charClass)
        {

            if(_charClass == "fighter")
            {
                mMaxHitPoints = 500;
                mCurrentHitPoints = 250;
                mCharClass = "Fighter";
            }

            else if (_charClass == "mage")
            {
                mMaxHitPoints = 250;
                mCurrentHitPoints = 125;
                mCharClass = "Mage";
            }

            else if(_charClass == "healer")
            {
                mMaxHitPoints = 120;
                mCurrentHitPoints = 60;
                mCharClass = "Healer";
            }

        }

    }
}
