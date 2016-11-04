//SpriteBatch will use texture unit 0
uniform sampler2D u_texture;
uniform vec2 u_screenResolution;

//"in" varyings from our vertex shader
varying vec4 vColor;
varying vec2 vTexCoord;
void main() {
   //sample the texture
   vec4 texColor = texture2D(u_texture, vTexCoord);
   float dx = u_screenResolution.x / 2.0 - gl_FragCoord.x;
   float dy = u_screenResolution.y / 2.0 - gl_FragCoord.y;
   float dxi = u_screenResolution.x / 2.0;
   float dyi = u_screenResolution.y / 2.0;
   float dis = sqrt(dx * dx + dy * dy);
   float disFull = sqrt(dxi * dxi + dyi * dyi);
   float disSq = (1-(dis / disFull));
   texColor.rgb = texColor.rgb * disSq * disSq * disSq * disSq * disSq;

   //final color
   gl_FragColor = texColor * vColor;
}