#!/usr/bin/python
# -*- coding: utf-8 -*-

# Copyright (C) 2012 Gabriel J. Pérez Irizarry 
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License, version 3,
# as published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program. If not, see <http://www.gnu.org/licenses/>.

from flask import Flask, render_template, request, redirect, url_for, jsonify

import json_app
import time
from random import randint

app = json_app.make_json_app('__main__')

@app.route('/')
def index():
    return render_template('welcome.html')

@app.route('/maps')
def maps():
    return render_template('maps.html')

@app.route('/table')
def table():
    return render_template('table.html')


@app.route('/get_data')
def ajax():

    old_timestamp = request.args.get('timestamp')
    print old_timestamp
    
    timestamp = time.time()

    locations = \
    [[33.77572,-84.39603],
    [33.77508,-84.40041],
    [33.77979,-84.40135],
    [33.77993,-84.40907],
    [33.77294,-84.39903],
    [33.77615,-84.39611],
    [33.77522,-84.38779],
    [33.78043,-84.38753],
    [33.78421,-84.38221]]

    return jsonify({'timestamp':timestamp, 'locations':[locations[randint(0,len(locations)-1)]]})

@app.route('/sign_in')
def sign_in():
    return render_template("index.html")


if __name__ == '__main__':
    app.run(debug=True)