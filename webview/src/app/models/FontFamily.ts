export enum FontFamily {
  Arial,
  CourierNew,
  TimesNewRoman,
  Verdana

}

export function fontFamilyToFontString(font: FontFamily): string {
    switch (font) {
      case FontFamily.Arial:
        return 'Arial';
      case FontFamily.CourierNew:
        return 'Courier New';
      case FontFamily.TimesNewRoman:
        return 'Times New Roman';
      case FontFamily.Verdana:
        return 'Verdana';
      default:
        return null;
    }
}
