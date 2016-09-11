#pragma version(1)
#pragma rs java_package_name(com.braisgabin.pokescreenshot.processing)

int HEIGHT_CP;
int VALUE_CP;
int VALUE_NO_CP;
rs_script script;

void root(const uchar4* in, uchar4* out, uint32_t x, uint32_t y) {
  *out = *in;
  uint32_t g = (uint32_t) ((in->r + in->g + in->b) / 3.);
  int value = y <= HEIGHT_CP ? VALUE_CP : VALUE_NO_CP;
  if (g <= value) {
    g = 0;
  } else {
    g = 255;
  }
  out->r = g;
  out->g = g;
  out->b = g;
}

void process(rs_allocation image, int heightCp, int valueCp, int valueNoCp) {
  HEIGHT_CP = heightCp;
  VALUE_CP = valueCp;
  VALUE_NO_CP = valueNoCp;
  rsForEach(script, image, image);
}
