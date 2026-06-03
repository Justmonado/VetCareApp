const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
    nombre:{
        type:String,
        required:true
    },
    apellido:{
        type:String,
        required:true
    },
    fechaNacimiento:{
        type:Date,
        required:true
    },
    correo:{
        type:String,
        required:true,
        unique:true
    },
    nombreUsuario:{
        type:String,
        required:true,
        unique:true
    },
    contraseña:{
        type:String,
        required:true
    }
});

module.exports = mongoose.model("Usuario", userSchema);