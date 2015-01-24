package modules;

import attributes.TimeManager;

/**
 * Created by William on Dec-12-2014.
 */
/*time: 12:17*/
public class EnvironmentManager {

    private MapEngine map;
    private Long frame;
    private TimeManager clock;

    public EnvironmentManager(MapEngine temp){
        changeMap(temp);
    }
    public void changeMap(MapEngine temp){
        map = temp;
        if(map.player.frame != null) {
            frame = map.player.frame.get();
        }else{
            frame = new Long(0);
            map.player.frame.set(frame);
        }
        clock = new TimeManager(frame);
    }
    public void setFrame(Long input){
        frame = input;
    }
    public TimeManager getTime(){
        return clock;
    }

}
