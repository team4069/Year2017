package org.usfirst.frc.team4069.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.Talon;

public class ControlTurret
{
  private CANTalon turretTalon;
  private StringBuilder sc_debug_info = new StringBuilder();
  private long mlastUpdateTime = 0;
  private double mWantedRPM = 3600;
  private int mEnabled = 0;
  private int mDebug = 0;
  private Robot mRobot;
  private LowPassFilter lpf=new LowPassFilter(200);
  
  public ControlTurret(Robot robot)
  {
    mRobot = robot;
    turretTalon = new CANTalon(1);
    mlastUpdateTime = System.currentTimeMillis();
  } // ShooterControl init

  public double GetShooterPosition()
  {
    return turretTalon.getPosition();
  }

  public void EnableDebug()
  {
    mDebug = 1;
  }

  public void DisableDebug()
  {
    mDebug = 0;
  }
  // --------------------------------------------------------------------------------------------
  /**
   * Linear Interpolation, given a value x2 between x0 and x1 calculate position between Y0 and Y1
   * 
   * @author EA
   */
  public double Lerp(double y0, double y1, double x0, double x1, double x2)
  {
    double y2 = y0 * (x2 - x1) / (x0 - x1) + y1 * (x2 - x0) / (x1 - x0);
    return y2;
  }
  public void Enable()
  {
    mEnabled = 1;
  }

  public void Disable()
  {
    mEnabled = 0;
  }

  public void Tick()
  {
    turretTalon.set(mRobot.driverStick.getAxis(AxisType.kY));
    double xpos = mRobot.vision_processor_instance.cregions.mXGreenLine;
    //double xpos = lpf.calculate(xxpos);
    
    if (xpos < 160)
    {
      double spd = Lerp(.15,.01,0,160,xpos);
      turretTalon.set(spd); //.15);
    }
    if (xpos > 160)
    {
      double spd = Lerp(-.15,-.01,320,160,xpos);
      turretTalon.set(spd);
    }
    if ((xpos >=150)&&(xpos <=170))
    {
      //turretTalon.set(0);
    }

  }
}