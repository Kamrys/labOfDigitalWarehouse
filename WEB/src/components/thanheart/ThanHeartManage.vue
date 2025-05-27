<template>
  <div id="jsi-cherry-container" class="wrapper">
    <canvas id="pinkBoard" class="wrapper"></canvas>
  </div>
</template>
  
  <script src="../../assets/js/jquery.min.js"></script>
  <script>
export default {
  name: "ThanHeartManagePage",
  data() {
    return {
      settings: {
        particles: {
          length: 500,
          duration: 2,
          velocity: 100,
          effect: -0.75,
          size: 30,
        },
      },
    };
  },
  created() {},
  mounted() {
    let canvas = document.getElementById("pinkBoard");
    this.drawCanvas(canvas);
    this.renderCanvasSize();
  },
  
  methods: {
    PointInit() {
      function Point(x, y) {
        this.x = typeof x !== "undefined" ? x : 0;
        this.y = typeof y !== "undefined" ? y : 0;
      }
      // 克隆point
      Point.prototype.clone = function () {
        return new Point(this.x, this.y);
      };
      // 计算点到原点距离
      Point.prototype.length = function (length) {
        // 未传入length，计算点到原点的距离
        if (typeof length == "undefined")
          return Math.sqrt(this.x * this.x + this.y * this.y);
        // 传入了length，获取该点等比例缩放至对应距离
        this.normalize();
        this.x *= length;
        this.y *= length;
        return this;
      };
      Point.prototype.normalize = function () {
        // 将点移动至距离原点为1的点
        let length = this.length();
        this.x /= length;
        this.y /= length;
        return this;
      };
      return Point;
    },
    /*
     * Particle class
     */
    ParticleInit() {
      let Point = this.PointInit();
      let that = this;
      function Particle() {
        /**
         * position: {x:0,y:0},
         * velocity: {x:0,y:0},
         * acceleration: {x:0,y:0},
         * age:0
         * @type {Point}
         */
        this.position = new Point();
        this.velocity = new Point();
        this.acceleration = new Point();
        this.age = 0;
      }
      Particle.prototype.initialize = function (x, y, dx, dy) {
        this.position.x = x;
        this.position.y = y;
        this.velocity.x = dx;
        this.velocity.y = dy;
        this.acceleration.x = dx * that.settings.particles.effect;
        this.acceleration.y = dy * that.settings.particles.effect;
        this.age = 0;
      };
      Particle.prototype.update = function (deltaTime) {
        // 根据时间增量变化位置及速度
        this.position.x += this.velocity.x * deltaTime;
        this.position.y += this.velocity.y * deltaTime;
        this.velocity.x += this.acceleration.x * deltaTime;
        this.velocity.y += this.acceleration.y * deltaTime;
        this.age += deltaTime;
      };
      Particle.prototype.draw = function (context, image) {
        function ease(t) {
          return --t * t * t + 1;
        }
        let size =
          image.width * ease(this.age / that.settings.particles.duration);
        context.globalAlpha = 1 - this.age / that.settings.particles.duration;
        context.drawImage(
          image,
          this.position.x - size / 2,
          this.position.y - size / 2,
          size,
          size
        );
      };
      return Particle;
    },

    /*
      ParticlePool class  微粒池
       */
    ParticlePoolInit() {
      let particles,
        firstActive = 0,
        firstFree = 0,
        duration = this.settings.particles.duration;
      let Particle = this.ParticleInit();
      function ParticlePool(length) {
        particles = new Array(length);
        for (let i = 0; i < particles.length; i++) {
          particles[i] = new Particle();
        }
      }
      ParticlePool.prototype.add = function (x, y, dx, dy) {
        particles[firstFree].initialize(x, y, dx, dy);
        firstFree++;
        if (firstFree === particles.length) firstFree = 0;
        if (firstActive === firstFree) firstActive++;
        if (firstActive === particles.length) firstActive = 0;
      };
      let index;
      ParticlePool.prototype.update = function (deltaTime) {
        if (firstActive < firstFree) {
          for (index = firstActive; index < firstFree; index++)
            particles[index].update(deltaTime);
        }
        if (firstFree < firstActive) {
          for (index = firstActive; index < particles.length; index++)
            particles[index].update(deltaTime);
          for (index = 0; index < firstFree; index++)
            particles[index].update(deltaTime);
        }

        while (
          particles[firstActive].age >= duration &&
          firstActive !== firstFree
        ) {
          firstActive++;
          if (firstActive === particles.length) firstActive = 0;
        }
      };

      ParticlePool.prototype.draw = function (context, image) {
        if (firstActive < firstFree) {
          for (index = firstActive; index < firstFree; index++)
            particles[index].draw(context, image);
        }
        if (firstFree < firstActive) {
          for (index = firstActive; index < particles.length; index++)
            particles[index].draw(context, image);
          for (index = 0; index < firstFree; index++)
            particles[index].draw(context, image);
        }
      };
      return ParticlePool;
    },

    drawCanvas(canvas) {
      let context = canvas.getContext("2d");
      let ParticlePool = this.ParticlePoolInit();
      let particles = new ParticlePool(this.settings.particles.length);
      let particleRate =
        this.settings.particles.length / this.settings.particles.duration;
      let time;
      let Point = this.PointInit();
      let that = this;

      function pointOnHeart(t) {
        return new Point(
          160 * Math.pow(Math.sin(t), 3),
          130 * Math.cos(t) -
            50 * Math.cos(2 * t) -
            20 * Math.cos(3 * t) -
            10 * Math.cos(4 * t) +
            25
        );
      }

      let image = (function () {
        let canvas = document.createElement("canvas"),
          context = canvas.getContext("2d");
        canvas.width = that.settings.particles.size;
        canvas.height = that.settings.particles.size;

        function to(t) {
          let point = pointOnHeart(t);
          point.x =
            that.settings.particles.size / 2 +
            (point.x * that.settings.particles.size) / 520;
          point.y =
            that.settings.particles.size / 2 -
            (point.y * that.settings.particles.size) / 520;
          return point;
        }
        context.beginPath();
        let t = -Math.PI;
        let point = to(t);
        context.moveTo(point.x, point.y);
        while (t < Math.PI) {
          t += 0.01;
          point = to(t);
          context.lineTo(point.x, point.y);
        }
        context.closePath();

        context.fillStyle = "#ea80b0";
        context.fill();

        let image = new Image();

        image.src = canvas.toDataURL();
        return image;
      })();

      function render() {
        requestAnimationFrame(render);

        let newTime = new Date().getTime() / 1000,
          deltaTime = newTime - (time || newTime);
        time = newTime;

        context.clearRect(0, 0, canvas.width, canvas.height);

        let amount = particleRate * deltaTime;

        for (let i = 0; i < amount; i++) {
          let pos = pointOnHeart(Math.PI - 2 * Math.PI * Math.random());
          let dir = pos.clone().length(that.settings.particles.velocity);
          particles.add(
            canvas.width / 2 + pos.x,
            canvas.height / 2 - pos.y,
            dir.x,
            -dir.y
          );
        }

        particles.update(deltaTime);

        particles.draw(context, image);
      }

      function onResize() {
        canvas.width = canvas.clientWidth;
        canvas.height = canvas.clientHeight;
      }

      window.onresize = onResize;

      setTimeout(function () {
        onResize();
        render();
      }, 10);
    },

    renderCanvasSize() {
      let canvas = document.getElementById("pinkBoard");
      let wrapper = document.getElementById("jsi-cherry-container");
      canvas.width = wrapper.clientWidth;
      canvas.height = wrapper.clientHeight;
    },
  },
};
</script>
  
  <style scoped>
#jsi-cherry-container {
  width: 100%;
  height: 100%;
  overflow: hidden;
  background-color: #000000;
}
</style>
  
  