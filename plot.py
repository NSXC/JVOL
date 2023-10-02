import pandas as pd
import matplotlib.pyplot as plt
df = pd.read_csv('data.csv')
plt.figure(figsize=(10, 6))
plt.plot(df['Time (s)'], df['Distance (ft)'], label='Distance (ft)')
plt.plot(df['Time (s)'], df['Height (ft)'], label='Height (ft)')
plt.xlabel('Time (s)')
plt.ylabel('Distance (ft) / Height (ft)')
plt.legend()
plt.title('Projectile Trajectory')
plt.grid(True)
plt.show()
