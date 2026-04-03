<template>
	<div class="app-wrapper">
		<index-header></index-header>
		<div class="main-wrapper">
			<index-aside></index-aside>
			<index-main></index-main>
		</div>
	</div>
</template>
<script>
	import IndexHeader from '@/components/index/IndexHeader'
	import IndexAside from '@/components/index/IndexAsideStatic'
	import IndexMain from '@/components/index/IndexMain'
	export default {
		components: {
			IndexHeader,
			IndexAside,
			IndexMain
		}
	}
</script>

<style lang="scss" scoped>
	.app-wrapper {
		width: 100%;
		height: 100vh;
		overflow: hidden;
	}

	.main-wrapper {
		display: flex;
		padding-top: 60px;
		height: 100vh;
		box-sizing: border-box;
	}
</style>
