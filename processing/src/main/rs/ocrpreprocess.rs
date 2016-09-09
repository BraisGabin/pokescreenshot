#pragma version(1)
#pragma rs java_package_name(com.braisgabin.pokescreenshot.processing)

int VALUE;

uchar4 RS_KERNEL ocrpreprocess(uchar4 in, uint32_t x, uint32_t y) {
  uchar4 out = in;
  uint32_t g = (uint32_t) ((in.r + in.g + in.b) / 3.);
  if (g <= VALUE) {
    g = 0;
  } else {
    g = 255;
  }
  out.r = g;
  out.g = g;
  out.b = g;
  return out;
}

void process(rs_allocation image, int value) {
  VALUE = value;
  rsForEach(ocrpreprocess, image, image);
}
