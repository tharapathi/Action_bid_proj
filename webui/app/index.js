var angular = require('angular');
var $ = require('jquery');

//hide and show container.
$( document ).ready(function() {
  $('#loginContainer').show();
  $('#respMessage').hide();
  $('#compileContainer').hide();

  $('#login-form-link').click(function(e) {
      $('#respMessage').hide();
      $("#login-form").delay(100).fadeIn(100);
      $("#register-form").fadeOut(100);
      $('#register-form-link').removeClass('active');
      $(this).addClass('active');
      e.preventDefault();
  });

  $('#register-form-link').click(function(e) {
      $('#respMessage').hide();
      $("#register-form").delay(100).fadeIn(100);
      $("#login-form").fadeOut(100);
      $('#login-form-link').removeClass('active');
      $(this).addClass('active');
      e.preventDefault();
  });

  $('#headerContainer').click(function(e){
    $('#login-form-link').click();
  });
});

// Angular app module.

var app = angular.module('app', [])
var baseURL = "http://localhost:8085/auction";

app.controller("LoginController", function ($scope, $http) {

    $scope.login = function () {
        // construct Json object.
        var dataObj = {
            "loginName": $scope.loginName,
            "password": $scope.password
        } 

        var config = {
            headers: {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        }
        //TODO: externalize base url.
        var res = $http.post(baseURL + '/user/login', dataObj, config);
        res.success(function (data, status, headers, config) {
            $('#respMessage').addClass('panel panel-success');
            $('#respMessage').show();
            $('#compileContainer').show();
            $('#LoginContainer').hide();
            $scope.respMessage = data.message;
        });

        res.error(function (data, status, headers, config) {
            $('#respMessage').addClass('panel panel-danger');
            $('#respMessage').show();
            $scope.respMessage = data.message;
        });
        // Making the fields empty
        $scope.loginName = '';
        $scope.password = '';
    };

    $scope.register = function () {
        // use $.param jQuery function to serialize data from JSON
        var dataObj = {
            "email": $scope.regEmail,
            "password": $scope.regPassword,
            "firstName": $scope.regFirstName,
            "lastName": $scope.regLastName,
            "contact": $scope.regContact,
            "address": $scope.regAddress
        } 

        var config = {
            headers: {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        }

        var res = $http.post(baseURL + '/user', dataObj, config);
        res.success(function (data, status, headers, config) {
            $('#respMessage').addClass('panel panel-success');
            $('#respMessage').show();
            $('#login-form-link').click();
            $scope.respMessage = data.message;
        });
        res.error(function (data, status, headers, config) {
            //alert( "failure message: " + JSON.stringify({data: data}));
            $('#respMessage').addClass('panel panel-danger');
            $('#respMessage').show();
            $scope.respMessage = data.message;
        });
        // Making the fields empty 
        $scope.regLoginName = '';
        $scope.regPassword = '';
        $scope.regEmail = '';
    };
});
