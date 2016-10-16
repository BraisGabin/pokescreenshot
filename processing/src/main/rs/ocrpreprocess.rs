#pragma version(1)
#pragma rs java_package_name(com.braisgabin.pokescreenshot.processing)

int HEIGHT_CP;
int HEIGHT_ARC;
int VALUE_DATA;
rs_script script;

void root(const uchar4* in, uchar4* out, uint32_t x, uint32_t y) {
  *out = *in;
  uint32_t g;
  if (y <= HEIGHT_CP) {
    g = (in->r != 255 || in->g != 255 || in->b != 255) ? 255 : 0;
  } else if (y <= HEIGHT_ARC) {
    g = (in->r != 255 || in->g != 255 || in->b != 255) ? 0 : 255;
  } else {
    if ((in->r + 4 * in->g + 2 * in->b) / 7 <= VALUE_DATA) {
      g = 0;
    } else {
      g = 255;
    }
  }
  out->r = g;
  out->g = g;
  out->b = g;
}

void process(rs_allocation image, int heightCp, int heightArc, int valueData) {
  HEIGHT_CP = heightCp;
  HEIGHT_ARC = heightArc;
  VALUE_DATA = valueData;
  rsForEach(script, image, image);
}
