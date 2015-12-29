package com.mobileapps.ngujjari.ticatacapp.highcomplexity;

public class Point {
	 int x, y;

	    public Point(int x, int y) {
	        this.x = x;
	        this.y = y;
	    }

		public int getNode()
		{
			if(x==1 && y==1) return 1;
            if(x==0 && y==2) return 2;
            if(x==1 && y==2) return 3;
            if(x==2 && y==2) return 4;
            if(x==2 && y==1) return 5;
            if(x==2 && y==0) return 6;
            if(x==1 && y==0) return 7;
            if(x==0 && y==0) return 8;
            if(x==0 && y==1) return 9;


			return -1;
		}
	    @Override
	    public String toString() {
	        return "[" + x + ", " + y + "]";
	    }
}
