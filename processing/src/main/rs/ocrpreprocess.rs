#pragma version(1)
#pragma rs java_package_name(com.braisgabin.pokescreenshot.processing)

int HEIGHT_CP;
int HEIGHT_ARC;
int VALUE_CP;
int VALUE_DATA;
rs_script script;

void root(const uchar4* in, uchar4* out, uint32_t x, uint32_t y) {
  *out = *in;
  uint32_t g;
  int value;
  if (y <= HEIGHT_CP) {
    g = 255 - ((in->r + in->g + in->b) / 3);
    value = 255 - VALUE_CP;
  } else if (y <= HEIGHT_ARC) {
    g = (in->r == 255 && in->g == 255 && in->b == 255) ? 255 : 0;
    value = 128;
  } else {
    g = (in->r + 4 * in->g + 2 * in->b) / 7;
    value = VALUE_DATA;
  }
  if (g <= value) {
    g = 0;
  } else {
    g = 255;
  }
  out->r = g;
  out->g = g;
  out->b = g;
}

void process(rs_allocation image, int heightCp, int heightArc, int valueCp, int valueData) {
  HEIGHT_CP = heightCp;
  HEIGHT_ARC = heightArc;
  VALUE_CP = valueCp;
  VALUE_DATA = valueData;
  rsForEach(script, image, image);
}
