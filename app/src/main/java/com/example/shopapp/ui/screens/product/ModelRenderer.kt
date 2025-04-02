package com.example.shopapp.ui.screens.product

import android.content.Context
import android.view.Surface
import android.view.SurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.filament.*
import com.google.android.filament.android.FilamentHelper
import com.google.android.filament.android.UiHelper
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.Channels
import kotlin.math.PI
import kotlin.math.sin

/**
 * Класс для рендеринга 3D-моделей с использованием Filament
 */
class ModelRenderer(private val context: Context) {
    private lateinit var engine: Engine
    private lateinit var renderer: Renderer
    private lateinit var scene: Scene
    private lateinit var view: View
    private lateinit var camera: Camera
    private lateinit var uiHelper: UiHelper
    
    private var swapChain: SwapChain? = null
    private var modelViewer: ModelViewer? = null
    
    private var animating = false
    private var surfaceView: SurfaceView? = null
    
    // Время последнего кадра для анимации
    private var lastFrameTimeNanos = 0L
    
    // Угол вращения модели
    private var rotation = 0f
    
    // Коллбэк для уведомления о загрузке модели
    private var onModelReady: () -> Unit = {}
    private var onModelLoading: (Boolean) -> Unit = {}
    
    // Инициализация Filament
    fun init(surfaceView: SurfaceView, onReady: () -> Unit, onLoading: (Boolean) -> Unit) {
        this.surfaceView = surfaceView
        this.onModelReady = onReady
        this.onModelLoading = onLoading
        
        uiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK)
        uiHelper.renderCallback = SurfaceCallback()
        uiHelper.attachTo(surfaceView)
        
        engine = Engine.create()
        renderer = engine.createRenderer()
        scene = engine.createScene()
        camera = engine.createCamera(engine.entityManager.create())
        
        view = engine.createView()
        view.scene = scene
        view.camera = camera
        
        // Настройка камеры
        camera.setExposure(16.0f, 1.0f / 125.0f, 100.0f)
        camera.lookAt(
            0.0, 0.0, 4.0, // наблюдатель
            0.0, 0.0, 0.0, // объект
            0.0, 1.0, 0.0  // верх
        )
    }
    
    // Загрузка модели из ресурсов
    fun loadModel(modelUrl: String) {
        onModelLoading(true)
        
        // В реальном приложении здесь должен быть код для загрузки модели из URL
        // Для примера, мы просто создаем простую геометрию
        
        // Создаем материал
        val material = Material.Builder()
            .package(readAsset("materials/basic.filamat"))
            .build(engine)
        
        // Создаем меш
        val vertexCount = 8
        val triangleCount = 12
        
        val vertexData = ByteBuffer.allocate(vertexCount * (3 + 3) * 4)
            .order(ByteOrder.nativeOrder())
            .apply {
                // Вершины куба
                putFloat(-1f); putFloat(-1f); putFloat(-1f)  // position
                putFloat(0f); putFloat(0f); putFloat(-1f)    // normal
                
                putFloat(1f); putFloat(-1f); putFloat(-1f)
                putFloat(0f); putFloat(0f); putFloat(-1f)
                
                putFloat(1f); putFloat(1f); putFloat(-1f)
                putFloat(0f); putFloat(0f); putFloat(-1f)
                
                putFloat(-1f); putFloat(1f); putFloat(-1f)
                putFloat(0f); putFloat(0f); putFloat(-1f)
                
                putFloat(-1f); putFloat(-1f); putFloat(1f)
                putFloat(0f); putFloat(0f); putFloat(1f)
                
                putFloat(1f); putFloat(-1f); putFloat(1f)
                putFloat(0f); putFloat(0f); putFloat(1f)
                
                putFloat(1f); putFloat(1f); putFloat(1f)
                putFloat(0f); putFloat(0f); putFloat(1f)
                
                putFloat(-1f); putFloat(1f); putFloat(1f)
                putFloat(0f); putFloat(0f); putFloat(1f)
            }
        
        // Индексы для граней куба
        val indexData = ByteBuffer.allocate(triangleCount * 3 * 2)
            .order(ByteOrder.nativeOrder())
            .apply {
                // Передняя грань
                putShort(0); putShort(1); putShort(2)
                putShort(2); putShort(3); putShort(0)
                
                // Задняя грань
                putShort(4); putShort(7); putShort(6)
                putShort(6); putShort(5); putShort(4)
                
                // Верхняя грань
                putShort(3); putShort(2); putShort(6)
                putShort(6); putShort(7); putShort(3)
                
                // Нижняя грань
                putShort(0); putShort(4); putShort(5)
                putShort(5); putShort(1); putShort(0)
                
                // Правая грань
                putShort(1); putShort(5); putShort(6)
                putShort(6); putShort(2); putShort(1)
                
                // Левая грань
                putShort(0); putShort(3); putShort(7)
                putShort(7); putShort(4); putShort(0)
            }
        
        vertexData.flip()
        indexData.flip()
        
        val indexType = IndexType.USHORT
        val vertexBuffer = VertexBuffer.Builder()
            .vertexCount(vertexCount)
            .bufferCount(1)
            .attribute(VertexBuffer.VertexAttribute.POSITION, 0, VertexBuffer.AttributeType.FLOAT3, 0, 24)
            .attribute(VertexBuffer.VertexAttribute.NORMAL, 0, VertexBuffer.AttributeType.FLOAT3, 12, 24)
            .build(engine)
        
        vertexBuffer.setBufferAt(engine, 0, vertexData)
        
        val indexBuffer = IndexBuffer.Builder()
            .indexCount(triangleCount * 3)
            .bufferType(indexType)
            .build(engine)
        
        indexBuffer.setBuffer(engine, indexData)
        
        // Создаем рендеризуемый компонент
        val renderable = EntityManager.get().create()
        
        RenderableManager.Builder(1)
            .boundingBox(Box(
                -1f, -1f, -1f,
                1f, 1f, 1f
            ))
            .material(0, material.defaultInstance)
            .geometry(0, RenderableManager.PrimitiveType.TRIANGLES, vertexBuffer, indexBuffer, 0, triangleCount * 3)
            .culling(false)
            .receiveShadows(true)
            .castShadows(true)
            .build(engine, renderable)
        
        scene.addEntity(renderable)
        
        // Симулируем загрузку модели
        surfaceView?.postDelayed({
            onModelLoading(false)
            onModelReady()
            startAnimation()
        }, 1000)
    }
    
    // Чтение файла из ассетов
    private fun readAsset(assetName: String): ByteBuffer {
        val input = context.assets.open(assetName)
        val bytes = input.readBytes()
        input.close()
        
        val buffer = ByteBuffer.allocateDirect(bytes.size)
            .order(ByteOrder.nativeOrder())
        buffer.put(bytes)
        buffer.flip()
        
        return buffer
    }
    
    // Запуск анимации вращения
    fun startAnimation() {
        if (animating) return
        
        animating = true
        lastFrameTimeNanos = System.nanoTime()
        
        // Запускаем анимационный цикл
        choreographer.postFrameCallback(frameCallback)
    }
    
    // Остановка анимации
    fun stopAnimation() {
        animating = false
    }
    
    // Коллбэк для анимации
    private val frameCallback = object : android.view.Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            if (!animating) return
            
            // Вычисляем дельту времени
            val deltaTimeNanos = if (lastFrameTimeNanos > 0) {
                frameTimeNanos - lastFrameTimeNanos
            } else 0
            
            lastFrameTimeNanos = frameTimeNanos
            
            // Обновляем анимацию
            updateAnimation(deltaTimeNanos / 1_000_000_000.0f)
            
            // Рендеринг кадра
            if (uiHelper.isReadyToRender) {
                renderer.render(view)
            }
            
            // Планируем следующий кадр
            choreographer.postFrameCallback(this)
        }
    }
    
    // Обновление анимации
    private fun updateAnimation(deltaTime: Float) {
        // Вращаем объект
        rotation += deltaTime * 50f
        
        // Находим первую сущность в сцене и вращаем её
        scene.entities.forEach { entity ->
            val transform = engine.transformManager
            if (transform.hasComponent(entity)) {
                transform.setTransform(
                    transform.getInstance(entity),
                    floatArrayOf(
                        cos(rotation), 0f, sin(rotation), 0f,
                        0f, 1f, 0f, 0f,
                        -sin(rotation), 0f, cos(rotation), 0f,
                        0f, 0f, 0f, 1f
                    )
                )
            }
        }
    }
    
    // Служебные функции для тригонометрии
    private fun cos(angleDegrees: Float): Float = kotlin.math.cos(angleDegrees * PI.toFloat() / 180f)
    private fun sin(angleDegrees: Float): Float = kotlin.math.sin(angleDegrees * PI.toFloat() / 180f)
    
    // Обработчик для поверхности отрисовки
    private inner class SurfaceCallback : UiHelper.RendererCallback {
        override fun onNativeWindowChanged(surface: Surface) {
            swapChain?.let { engine.destroySwapChain(it) }
            swapChain = engine.createSwapChain(surface)
        }
        
        override fun onDetachedFromSurface() {
            swapChain?.let {
                engine.destroySwapChain(it)
                engine.flushAndWait()
                swapChain = null
            }
        }
        
        override fun onResized(width: Int, height: Int) {
            view.viewport = Viewport(0, 0, width, height)
            val aspect = width.toDouble() / height.toDouble()
            camera.setProjection(45.0, aspect, 0.1, 100.0, Camera.Fov.VERTICAL)
        }
    }
    
    // Освобождение ресурсов
    fun release() {
        stopAnimation()
        
        swapChain?.let { engine.destroySwapChain(it) }
        engine.destroyRenderer(renderer)
        engine.destroyView(view)
        engine.destroyScene(scene)
        engine.destroyCamera(camera.entity)
        engine.destroy()
    }
    
    // Синглтон для Choreographer
    private val choreographer: android.view.Choreographer
        get() = android.view.Choreographer.getInstance()
}

/**
 * Composable для отображения 3D-модели
 */
@Composable
fun ModelView(
    modelUrl: String,
    modifier: Modifier = Modifier,
    onModelReady: () -> Unit = {},
    onModelLoading: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    
    // Создаем рендерер
    val renderer = remember { ModelRenderer(context) }
    
    // Отображаем SurfaceView для рендеринга модели
    AndroidView(
        factory = { ctx ->
            SurfaceView(ctx).apply {
                renderer.init(this, onModelReady, onModelLoading)
                renderer.loadModel(modelUrl)
            }
        },
        modifier = modifier
    )
    
    // Очищаем ресурсы при исчезновении компонента
    DisposableEffect(Unit) {
        onDispose {
            renderer.release()
        }
    }
} 