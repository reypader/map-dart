/*global module:false*/
module.exports = function (grunt) {

  var globalConfig = {
    images: 'app/images',
    css: 'app/styles',
    fonts: 'app/fonts',
    scripts: 'app/modules',
    bower_path: 'bower_components'
  };
  // Project configuration.
  grunt.initConfig({
      globalConfig: globalConfig,
      clean: ['dist', '.tmp', '<%= globalConfig.fonts %>/*.*', 'app/require.js', '<%= globalConfig.css %>/compiled-bootstrap', '<%= globalConfig.css %>/*.*'],
      jshint: {
        options: {
          curly: true,
          eqeqeq: true,
          immed: true,
          latedef: true,
          newcap: true,
          noarg: true,
          sub: true,
          undef: true,
          unused: true,
          boss: true,
          eqnull: true,
          globals: {}
        },
        all: {
          src: [
            'Gruntfile.js',
            '<%= globalConfig.scripts %>/**/*.js'
          ]
        },
        test: {
          src: ['test/spec/**/*.js']
        }
      },
      customize_bootstrap: {
        task: {
          options: {
            bootstrapPath: '<%= globalConfig.bower_path %>/bootstrap',
            src: '<%= globalConfig.css %>/customized-bootstrap-less',
            dest: '<%= globalConfig.css %>/compiled-bootstrap',
          }
        }
      },
      less: {
        task: {
          src: '<%= customize_bootstrap.task.options.dest %>/bootstrap.less',
          dest: '<%= customize_bootstrap.task.options.dest %>/bootstrap.css'
        }
      },
      requirejs: {
        compile: {
          options: {
            baseUrl: '<%= globalConfig.scripts %>',
            mainConfigFile: '<%= globalConfig.scripts %>/main.js',
            name: 'main',
            out: 'dist/modules/main.js',
            optimize: "uglify",
            preserveLicenseComments: false
          }
        }
      },
      bowerRequirejs: {
        target: {
          rjsConfig: '<%= globalConfig.scripts %>/main.js',
          options: {
            transitive: true,
            exclude: [
              'requirejs-plugins'
            ]
          }
        }
      },
      copy: {
        pages: {
          files: [
            {
              expand: true,
              cwd: 'app',
              src: ['**/*.html'],
              dest: 'dist/',
              filter: 'isFile'
            }
          ]
        },
        statics: {
          files: [
            {
              expand: true,
              cwd: 'app',
              src: ['fonts/**', 'images/**'],
              dest: 'dist/',
              filter: 'isFile'
            }
          ]
        },
        require: {
          files: [
            {
              expand: true,
              cwd: 'app',
              src: ['require.js'],
              dest: 'dist/',
              filter: 'isFile'
            }
          ]
        },
        init: {
          files: [
            {
              expand: true,
              flatten: true,
              src: ['node_modules/requirejs/require.js'],
              dest: 'app/',
              filter: 'isFile'
            },
            //{
            //  expand: true,
            //  flatten: true,
            //  src: ['bower_components/bootstrap/dist/css/bootstrap-theme.min.css'],
            //  dest: '<%= globalConfig.css %>/raw',
            //  filter: 'isFile'
            //},
            {
              expand: true,
              flatten: true,
              src: ['<%= globalConfig.bower_path %>/bootstrap/dist/fonts/*'],
              dest: '<%= globalConfig.fonts %>/',
              filter: 'isFile'
            },
            {
              expand: true,
              flatten: true,
              src: ['<%= globalConfig.bower_path %>/font-awesome/fonts/*'],
              dest: '<%= globalConfig.fonts %>/',
              filter: 'isFile'
            }
          ]
        }
      },
      useminPrepare: {
        html: ['app/**/*.html'],
        options: {
          dest: 'dist'
        }
      },
      usemin: {
        html: ['dist/**/*.html']
      },
      filerev: {
        dist: {
          src: [
            'dist/styles/**/*.css',
          ]
        }
      },
      karma: {
        unit: {
          configFile: 'karma.conf.js',
          singleRun: true
        }
      },
      connect: {
        options: {
          port: 9000,
          livereload: 35729,
          hostname: 'localhost' // * = accessible from anywhere ; default: localhost
        },
        livereload: {
          options: {
            //open: true,
            keepAlive: true,
            base: 'dist',
            protocol: 'https',
            key: grunt.file.read('dev_keys/server.key').toString(),
            cert: grunt.file.read('dev_keys/server.crt').toString(),
            ca: grunt.file.read('dev_keys/ca.crt').toString(),
            middleware: function (connect) {
              return [
                connect().use(
                  '/bower_components',
                  connect.static('./bower_components')
                ),
                connect.static('./app')
              ];
            }
          }
        }
      },
      watch: {
        livereload: {
          options: {
            livereload: {
              port: '<%= connect.options.livereload %>',
              key: grunt.file.read('dev_keys/server.key').toString(),
              cert: grunt.file.read('dev_keys/server.crt').toString(),
              ca: grunt.file.read('dev_keys/ca.crt').toString(),
            }
          },
          files: [
            'app/modules/**/*.js',
            'app/styles/raw/*.css',
            'app/styles/customized-bootstrap-less/*.less',
            'bower.json'
          ],
          tasks: ['bowerRequirejs', 'test', 'compile-style']
        }
      }
    }
  );

  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-requirejs');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-connect');
  grunt.loadNpmTasks('grunt-contrib-cssmin');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-filerev');
  grunt.loadNpmTasks('grunt-bower-requirejs');
  grunt.loadNpmTasks('grunt-customize-bootstrap');
  grunt.loadNpmTasks('grunt-karma');
  grunt.loadNpmTasks('grunt-usemin');


  grunt.registerTask('serve', ['connect', 'watch']);
  grunt.registerTask('test', ['init', 'karma']);
  grunt.registerTask('release', ['test', 'requirejs', 'copy:pages', 'copy:require', 'copy:statics', 'build-pages']);
  grunt.registerTask('compile-style', ['customize_bootstrap', 'less']);
  grunt.registerTask('init', ['clean', 'copy:init', 'bowerRequirejs', 'compile-style']);

  grunt.registerTask('build-pages', [
    'useminPrepare',
    'concat:generated',
    'cssmin:generated',
    'filerev',
    'usemin'
  ]);

};
