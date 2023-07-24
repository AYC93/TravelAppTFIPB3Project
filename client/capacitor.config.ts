import { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'work.shiok.planners',
  appName: 'shiok-planners',
  webDir: 'dist',
  server: {
    androidScheme: 'https'
  }
};

export default config;
