#pragma version(1)
#pragma rs java_package_name(com.braisgabin.pokescreenshot.processing)

int VALUE;
rs_script script;

void root(const uchar4* in, uchar4* out, uint32_t x, uint32_t y) {
  *out = *in;
  uint32_t g = (uint32_t) ((in->r + in->g + in->b) / 3.);
  if (g <= VALUE) {
    g = 0;
  } else {
    g = 255;
  }
  out->r = g;
  out->g = g;
  out->b = g;
}

void process(rs_allocation image, int value) {
  VALUE = value;
  rsForEach(script, image, image);
}
