import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class JVOL {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        final double GRAVITY = 32.17405; // Earth's gravity (ft/s^2)
        final double AIR_DENSITY = 0.0023769; // Air density at sea level (slug/ft^3)
        final double PROJECTILE_MASS = 0.0022; // Mass of the projectile (lbm)
        final double PROJECTILE_RADIUS = 0.01; // Radius of the projectile (ft)
        final double TIME_STEP = 0.01; // Time step (s)

        System.out.print("Enter the initial speed (mph): ");
        double initialSpeedMph = scanner.nextDouble();

        System.out.print("Enter the launch angle (degrees): ");
        double launchAngle = Math.toRadians(scanner.nextDouble());

        System.out.print("Enter the target distance (ft): ");
        double targetDistanceFt = scanner.nextDouble();

        double initialSpeedFtPerSec = initialSpeedMph * 1.46667;

        double initialVelocityX = initialSpeedFtPerSec * Math.cos(launchAngle);
        double initialVelocityY = initialSpeedFtPerSec * Math.sin(launchAngle);

        double time = 0.0;
        double distance = 0.0;
        double xVelocity = initialVelocityX;
        double yVelocity = initialVelocityY;
        double xPosition = 0.0;
        double yPosition = 0.0;
        boolean reachedTarget = false;

        try (FileWriter csvWriter = new FileWriter("data.csv")) {
            csvWriter.write("Time (s),Distance (ft),Height (ft)\n");

            while (xPosition < targetDistanceFt) {
                double velocity = Math.sqrt(xVelocity * xVelocity + yVelocity * yVelocity);
                double airResistance = -0.5 * AIR_DENSITY * velocity * velocity * Math.PI * PROJECTILE_RADIUS * PROJECTILE_RADIUS;
                double xAcceleration = airResistance * xVelocity / (velocity * PROJECTILE_MASS);
                double yAcceleration = -GRAVITY - (airResistance * yVelocity / (velocity * PROJECTILE_MASS));
                xVelocity += xAcceleration * TIME_STEP;
                yVelocity += yAcceleration * TIME_STEP;
                xPosition += xVelocity * TIME_STEP;
                yPosition += yVelocity * TIME_STEP;
                time += TIME_STEP;
                distance = xPosition;

                csvWriter.write(String.format("%.2f,%.2f,%.2f\n", time, distance, yPosition));

                if (yPosition < 0 || xPosition > targetDistanceFt) {
                    reachedTarget = xPosition >= targetDistanceFt;
                    break;
                }
            }

            if (reachedTarget) {
                System.out.println("Projectile reached the target.");
            } else {
                System.out.println("Projectile fell short by " + String.format("%.2f", (targetDistanceFt - xPosition)) + " ft.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
