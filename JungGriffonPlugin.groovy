/*
 * Copyright 2009-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Andres Almiray
 */
class JungGriffonPlugin {
    // the plugin version
    String version = '0.5'
    // the version or versions of Griffon the plugin is designed for
    String griffonVersion = '0.9.5 > *'
    // the other plugins this plugin depends on
    Map dependsOn = [swing: '0.9.5']
    // resources that are included in plugin packaging
    List pluginIncludes = []
    // the plugin license
    String license = 'Apache Software License 2.0'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    List toolkits = ['swing']
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    List platforms = []
    // URL where documentation can be found
    String documentation = ''
    // URL where source can be found
    String source = 'https://github.com/griffon/griffon-jung-plugin'

    List authors = [
        [
            name: 'Andres Almiray',
            email: 'aalmiray@yahoo.com'
        ]
    ]
    String title = 'Graph visualizations with JUNG'
    String description = '''
[JUNG][1] — the Java Universal Network/Graph Framework--is a software library that provides a common and extendible language for the modeling,
analysis, and visualization of data that can be represented as a graph or network.

Usage
-----

The following nodes will become available on a View script upon installing this plugin

| Node                     | Property    | Type   | Default | Required | Bindable | Notes                                                            |
| ------------------------ | ----------- | ------ | ------- | -------- | -------- | ---------------------------------------------------------------- |
| basicVisualizationViewer | graph       | Graph  |         | yes      | no       | either set a value for this property or set a value for the node |
|                          | graphLayout | Layout |         | no       | no       | either set a value for this property or set a value for the node |
| visualizationViewer      | graph       | Graph  |         | yes      | no       | either set a value for this property or set a value for the node |
|                          | graphLayout | Layout |         | no       | no       | either set a value for this property or set a value for the node |
| balloonLayout            | graph       | Forest |         | yes      | no       | either set a value for this property or set a value for the node |
| circleLayout             | graph       | Graph  |         | yes      | no       | either set a value for this property or set a value for the node |
| dagLayout                | graph       | Graph  |         | yes      | no       | either set a value for this property or set a value for the node |
| frLayout                 | graph       | Graph  |         | yes      | no       | either set a value for this property or set a value for the node |
| frLayout2                | graph       | Graph  |         | yes      | no       | either set a value for this property or set a value for the node |
| isomLayout               | graph       | Graph  |         | yes      | no       | either set a value for this property or set a value for the node |
| kkLayout                 | graph       | Graph  |         | yes      | no       | either set a value for this property or set a value for the node |
| radialTreeLayout         | graph       | Forest |         | yes      | no       | either set a value for this property or set a value for the node |
| springLayout             | graph       | Graph  |         | yes      | no       | either set a value for this property or set a value for the node |
| springLayout2            | graph       | Graph  |         | yes      | no       | either set a value for this property or set a value for the node |
| staticLayout2            | graph       | Graph  |         | yes      | no       | either set a value for this property or set a value for the node |
| treeLayout               | graph       | Forest |         | yes      | no       | either set a value for this property or set a value for the node |
| defaultModalGraphMouse   |             |        |         |          |          | nest it inside a `visualizationViewer`                           |

You can set a Graph value or graph: property on each layout node, if not specified it will use its parent's graph.
This plugin includes a few vertex decorators that will allow you to tweak a vertex's shape:

**griffon.jung.visualization.decorators.BasicVertexShapeTransformer**
Constructor signatures are:

 *  BasicVertexShapeTransformer(groovy.lang.Closure shapeTransformer, boolean cacheShapes = true)
 *  BasicVertexShapeTransformer(Transformer&lt;V,Shape&gt; shapeTransformer, boolean cacheShapes = true)
 *  BasicVertexShapeTransformer(Transformer vsf&lt;V,Integer&gt;, Transformer&lt;V,Float&gt; varf, groovy.lang.Closure shapeTransformer, boolean cacheShapes = true)
 *  BasicVertexShapeTransformer(Transformer vsf&lt;V,Integer&gt;, Transformer&lt;V,Float&gt; varf, Transformer&lt;V,Shape&gt; shapeTransformer, boolean cacheShapes = true)

Either set a `Closure` or a `Transformer&lt;V,Shape&gt;` that hols the logic of computing a vertex's (v) shape. This decorator will cache all generated shapes by default.

**griffon.jung.visualization.decorators.TemplateVertexShapeTransformer**
Constructor signatures are:

 *  TemplateVertexShapeTransformer(Shape template = new Rectangle2D.Double(0d,0d,1d,1d))
 *  TemplateVertexShapeTransformer(Transformer vsf&lt;V,Integer&gt;, Transformer&lt;V,Float&gt; varf, Shape template = new Rectangle2D.Double(0d,0d,1d,1d))

You just need to set a template Shape. All shapes will be cached.

**Simplified Transitive Properties**
Both `basicVisualizationViewer` and `visualizationViewer` will attempt setting properties on their respective `RenderContext` and `Renderer`.
This simplifies greatly the amount of code you'd had to write otherwise. The following example demonstrates this feature.

Here is an example graph visualization that uses `TemplateVertexShapeTransformer` coupled with [jSilhouette's'][2] `Star` shape, it also attaches
a few decorators and helpers.

        import edu.uci.ics.jung.graph.Graph
        import edu.uci.ics.jung.graph.SparseMultigraph
        import edu.uci.ics.jung.visualization.decorators.ToStringLabeller
        import edu.uci.ics.jung.visualization.renderers.Renderer
        import org.apache.commons.collections15.Transformer
        import org.apache.commons.collections15.functors.ConstantTransformer
        import java.awt.Color
        import org.codehaus.griffon.jsilhouette.geom.Star
        import griffon.plugins.jung.visualization.decorators.TemplateVertexShapeTransformer
 
        int max = 10
        Graph g = new SparseMultigraph()
        (1..max).each { Integer i -> g.addVertex(i) }
        (1..<max).each { Integer x -> g.addEdge("Edge-$x", x, x+1) }
        g.addEdge("Edge-${max}", max, 1)
 
        def colorMap = [:]
        Random r = new Random()
        def paintTransformer = { Integer i ->
          colorMap.get(i, new Color(r.nextInt(64)+192i, 0i, 0i))
        } as Transformer
 
        bean(id: "star", new Star(), or: 40f, ir: 20f, count: 5i)
        bean(id: "starrer", new TemplateVertexShapeTransformer(star), sizeTransformer: new ConstantTransformer(40i))
 
        application(title: 'JUNG + Griffon',
          pack: true,
          locationByPlatform:true,
          iconImage: imageIcon('/griffon-icon-48x48.png').image,
          iconImages: [imageIcon('/griffon-icon-48x48.png').image,
                       imageIcon('/griffon-icon-32x32.png').image,
                       imageIcon('/griffon-icon-16x16.png').image]) {
          visualizationViewer(g, id: "gview", preferredSize: [350,350],
               vertexShapeTransformer: starrer, edgeLabelTransformer: new ToStringLabeller(),
               vertexLabelTransformer: new ToStringLabeller(), vertexFillPaintTransformer: paintTransformer) {
            circleLayout(size: [330, 330])
            defaultModalGraphMouse()
          }
          gview.renderer.vertexLabelRenderer.position = Renderer.VertexLabel.Position.CNTR
        }

For more information consult [JUNG's Documentation page][3], the tutorial is quite good!

[1]: http://jung.sourceforge.net/
[2]: http://griffon.codehaus.org/jsilhouette
[3]: http://jung.sourceforge.net/doc/index.html
'''
}
