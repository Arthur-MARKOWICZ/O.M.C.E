export const palettes = {
  light: {
    ink: '#153128', muted: '#6b7a73', line: '#dbe5df', paper: '#fbfcf9', surface: '#ffffff',
    surfaceMuted: '#edf1ed', green: '#176b42', greenDark: '#0d492c', lime: '#d3ea5a',
    cream: '#f2f4e9', danger: '#b53333', copy: '#53655c', input: '#c9d6ce', onAction: '#ffffff',
  },
  dark: {
    ink: '#ecf5ef', muted: '#aabbb1', line: '#30443b', paper: '#101713', surface: '#19231e',
    surfaceMuted: '#223128', green: '#7fd6a3', greenDark: '#b1e8c6', lime: '#d9ed72',
    cream: '#17231d', danger: '#ff8d8d', copy: '#c2d1c8', input: '#40574b', onAction: '#ffffff',
  },
} as const;

export type ThemeName = keyof typeof palettes;
export type Colors = (typeof palettes)[ThemeName];

export const categoryOptions = ['ESP32', 'ARDUINO', 'REGISTORES', 'SENSORES', 'BATERIA', 'CABOS', 'MOTORES', 'CONECTORES', 'OUTRO'];
